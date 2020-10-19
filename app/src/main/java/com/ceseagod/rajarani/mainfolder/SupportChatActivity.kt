package com.ceseagod.rajarani.mainfolder

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.supchatscreenadapter
import com.ceseagod.showcase.support_page.supportmodel

import com.ceseagod.rajarani.utilities.SessionMaintainence
import com.google.firebase.Timestamp
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_support_chat.*

import java.io.ByteArrayOutputStream

class SupportChatActivity : AppCompatActivity() {
    var sessionMaintainence = SessionMaintainence.instance
    var firestoreDB: FirebaseFirestore? = null
    private var firestoreListener: ListenerRegistration? = null
    var progress: ProgressDialog? = null
    private val FINAL_CHOOSE_PHOTO = 2
    private val FINAL_TAKE_PHOTO = 1
    var bitmap: Bitmap? = null
    private var imageUri: Uri? = null
    var id: String = ""
    var imagePath: String = ""
    var imageLinks: String = ""
    var status: String = ""

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_chat)
        firestoreDB = FirebaseFirestore.getInstance()
        id = intent.getStringExtra("id")
        status = intent.getStringExtra("status")
        if (status == "closed") {
            linearLayout2.visibility = View.INVISIBLE
            reopen.visibility = View.VISIBLE
            reopen.setBackgroundResource(R.drawable.paymentb)

            reopen.setOnClickListener {
                changestatus()
            }
        }


        messageedit.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (messageedit.text.toString() == "") {
                    sendmessage.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                    sendmessage.isClickable = false
                } else {
                    sendmessage.setImageResource(R.drawable.sendbutton)
                    sendmessage.isClickable = true

                }
            }
        })
        image.setOnClickListener {
            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
                selectImageInAlbum()             }
        }
        sendmessage.setOnClickListener {
            val msg = messageedit.text.toString()
            messageedit.setText("")
            val privateDataRef =
                firestoreDB!!.collection("support").document(id)
            privateDataRef
                .update(
                    mapOf(
                        "message" to (FieldValue.arrayUnion(
                            mapOf(
                                "timeStamp" to Timestamp.now(),
                                "messages" to msg,
                                "user_id" to sessionMaintainence!!.Uid!!,
                                "type_one" to "text",
                                "type" to sessionMaintainence!!.userType

                            )
                        )),
                        "type" to sessionMaintainence!!.userType
                    )
                )
                //.set(data, SetOptions.merge())
                .addOnSuccessListener {
                    return@addOnSuccessListener
                }
                .addOnFailureListener { e ->
                    return@addOnFailureListener
                }
        }

        firestoreListener = firestoreDB!!.collection("support").document(id)
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    return@EventListener
                }
                try {
                    val ress = documentSnapshots!!.toObject(supportmodel::class.java)
                    val mwss = ress!!.message

                    res.layoutManager = LinearLayoutManager(this)
                    val adapters =
                        supchatscreenadapter(ress, mwss, this)
                    res.adapter = adapters
                    res.scrollToPosition(adapters.itemCount - 1)
                } catch (e: Exception) {
                }
            })

    }

    private fun changestatus() {
        progress = ProgressDialog(this)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val privateDataRef =
            firestoreDB!!.collection("support").document(id)
        privateDataRef
            .update(
                mapOf(
                    "status" to "open"
                )
            )
            .addOnSuccessListener {
                linearLayout2.visibility = View.VISIBLE
                reopen.visibility = View.GONE
                progress!!.dismiss()
            }
            .addOnFailureListener {
                progress!!.dismiss()

            }

    }

    override fun onDestroy() {
        super.onDestroy()
        firestoreListener!!.remove()
    }

    private fun photouploadFile(bitmap: Bitmap, folder: String, userId: String) {
        progress = ProgressDialog(this)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("support")
        val mountainImagesRef =
            storageRef.child(folder + "/" + userId + Timestamp.now().toString() + ".jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            progress!!.dismiss()
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                imageLinks = it.toString()
                progress!!.dismiss()
                addImage(imageLinks)
            }
        }


    }

    private fun addImage(imageLinks: String) {
        progress = ProgressDialog(this)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        firestoreDB!!.collection("support").document(id)
            .update(
                mapOf(
                    "message" to (FieldValue.arrayUnion(
                        mapOf(
                            "timeStamp" to Timestamp.now(),
                            "messages" to imageLinks,
                            "user_id" to sessionMaintainence!!.Uid!!,
                            "type_one" to "image",
                            "type" to sessionMaintainence!!.userType

                        )
                    )),
                    "type" to sessionMaintainence!!.userType
                )
            )
            .addOnSuccessListener {
                progress!!.dismiss()
            }
            .addOnFailureListener {
                progress!!.dismiss()

            }

    }


    private fun openAlbum() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, FINAL_CHOOSE_PHOTO)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImageInAlbum()
                } else {
                    Toast.makeText(this, "You deied the permission", Toast.LENGTH_SHORT).show()
                }
        }
    }
    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent,OPEN_DOCUMENT_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                // this is the image selected by the user
                imageUri = resultData.data
                val bitmap = MediaStore.Images.Media.getBitmap(this!!.contentResolver, imageUri)
                handleImageOnKitkat(resultData,bitmap)
//                uploadFile(bitmap, SessionMaintainence.instance!!.Uid!!)
//                val dialog =
//                    BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme) // Style here
//                val view = layoutInflater.inflate(R.layout.profile_bottom_sheet_dialog, null)
//                dialog.setContentView(view)
//                dialog.payimages.setImageURI(imageUri)
            }
        }
    }

    @TargetApi(19)
    private fun handleImageOnKitkat(data: Intent?, bitmap: Bitmap) {
        val uri = data!!.data
        if (DocumentsContract.isDocumentUri(this, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri!!.authority) {
                val id = docId.split(":")[1]
                val selsetion = MediaStore.Images.Media._ID + "=" + id
                imagePath = imagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selsetion)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(docId)
                )
                imagePath = imagePath(contentUri, null)
            }
        } else if ("content".equals(uri!!.scheme, ignoreCase = true)) {
            imagePath = imagePath(uri, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            imagePath = uri!!.path!!
        }
        photouploadFile(bitmap!!, "supportImage", sessionMaintainence!!.Uid!!)
    }

    private fun imagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = contentResolver.query(uri!!, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }
    companion object{
        private const val OPEN_DOCUMENT_CODE = 3

    }
}
