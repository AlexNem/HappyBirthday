package com.alexnemyr.happybirthday.ui.common.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.alexnemyr.domain.util.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

data class Media(
    val uri: Uri,
    val name: String,
    val size: Long,
    val mimeType: String,
)

suspend fun getImages(contentResolver: ContentResolver): List<Media> = withContext(Dispatchers.IO) {
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.MIME_TYPE,
    )

    val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Query all the device storage volumes instead of the primary only
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val images = mutableListOf<Media>()

    contentResolver.query(
        collectionUri,
        projection,
        null,
        null,
        "${MediaStore.Images.Media.DATE_ADDED} DESC"
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
        val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

        while (cursor.moveToNext()) {
            val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))
            val name = cursor.getString(displayNameColumn)
            val size = cursor.getLong(sizeColumn)
            val mimeType = cursor.getString(mimeTypeColumn)

            val image = Media(uri, name, size, mimeType)
            images.add(image)
        }
    }

    return@withContext images
}

fun getMediaFile(context: Context, uri: Uri): Media? {
    var image : Media? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(
            uri,
            null,
            null,
            null,
//            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )
        cursor?.use {
            if (it.moveToFirst()) {
//                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
//                val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

//                val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))
                val name = cursor.getString(displayNameColumn)
                val size = cursor.getLong(sizeColumn)
                val mimeType = "image/jpeg"//cursor.getString(mimeTypeColumn)

                image = Media(uri, name, size, mimeType)
            }
        }
    }
    return image
}

suspend fun getUri(
    uriPath: String?,
    context: Context,
): Uri? {
    return runCatching {

        val pathImageName = uriPath?.split("/")?.lastOrNull()


        val photopickerIndexKey = "/document/"
        val uriIndex = uriPath?.indexOfAny(listOf(photopickerIndexKey), 0, false)
        val uriResult = uriPath?.reversed()?.take(2)?.reversed()
        val getImages = getImages(context.contentResolver)
        val imageNamesMap = getImages.map {
            val mediaImageName = it.uri.encodedPath?.split("/")?.lastOrNull()

            val indexKey = "/media/"
            val mediaIndexOne = it.uri.encodedPath?.indexOfAny(listOf(indexKey), 0, false)
            val mediaResult =
                it.uri.encodedPath?.substring(mediaIndexOne?.plus(indexKey.length) ?: 0)
            Timber.tag(TAG).e(
                "Photo -> forEach = " + "\nuriPath = $uriPath" + "\nmediaPath = ${it.uri.encodedPath}" + "\nmediaResult = $mediaResult" + "\nuriIndex = $uriIndex" + "\nuriResult = $uriResult" + "\npathImageName = $pathImageName" + "\nmediaImageName = $mediaImageName" + "\nsplit = ${
                    uriPath?.split(
                        "/"
                    )
                }" + "\n${it.uri.path?.indexOfAny(listOf(indexKey), 0, false)}"
            )

            mapOf(Pair(mediaResult, it.uri))
        }.apply {
            Timber.tag(TAG).e("Photo -> apply = $this")
        }

        val selectedImage = imageNamesMap.find { it.containsKey(uriResult) }
        Timber.tag(TAG).e("Photo -> getImages = ${getImages}")
        Timber.tag(TAG).e("Photo -> selectedImage = ${selectedImage}")
        return selectedImage?.values?.firstOrNull()
    }.onFailure {
        Timber.tag(TAG).e("Photo -> onFailure = ${it.message}")
    }.getOrNull()
}

fun uriToBitmap(selectedFileUri: Uri, context: Context): Bitmap? {
    try {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(selectedFileUri, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    } catch (e: IOException) {
        Timber.tag(TAG).e("uriToBitmap -> catch = $e")
        e.printStackTrace()
    }
    return null
}

fun fileFromContentUri(context: Context, contentUri: Uri): File {

    val fileExtension = getFileExtension(context, contentUri)
    val fileName = "temporary_file" + if (fileExtension != null) ".$fileExtension" else ""

    val tempFile = File(context.cacheDir, fileName)
    tempFile.createNewFile()

    try {
        val oStream = FileOutputStream(tempFile)
        val inputStream = context.contentResolver.openInputStream(contentUri)

        inputStream?.let {
            copy(inputStream, oStream)
        }

        oStream.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return tempFile
}

private fun getFileExtension(context: Context, uri: Uri): String? {
    val fileType: String? = context.contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}