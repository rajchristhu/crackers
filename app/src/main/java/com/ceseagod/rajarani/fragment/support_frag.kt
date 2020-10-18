package com.ceseagod.rajarani.fragment

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ceseagod.rajarani.MainActivity
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.adapter.supportAdapter
import com.ceseagod.rajarani.utilities.elements
import com.ceseagod.showcase.support_page.supportmessagemodel
import com.ceseagod.showcase.support_page.supportmodel
import com.ceseagod.showcase.utilities.SessionMaintainence
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.create_ticket.*
import kotlinx.android.synthetic.main.support_frag_fragment.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.util.*

class support_frag(val mainActivity: MainActivity) : Fragment() {
    private val FINAL_CHOOSE_PHOTO = 2
    private val FINAL_TAKE_PHOTO = 1
    var bitmap: Bitmap? = null
    private var mWordViewModel: MainViewModel? = null

    var sessionMaintainence = SessionMaintainence.instance
    var firestoreDB: FirebaseFirestore? = null
    var des: String = ""
    var imageLinks: String = ""
    private var imageUri: Uri? = null
    var imagePath: String? = ""
    var progress: ProgressDialog? = null
    val supportList = mutableListOf<supportmodel>()
    var supportmsgList: ArrayList<supportmessagemodel>? = null
    var tr: ArrayList<String>? = null

    companion object {
//        fun newInstance() = support_frag(this)
        private const val OPEN_DOCUMENT_CODE = 3

    }

    private lateinit var viewModel: SupportFragViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.support_frag_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SupportFragViewModel::class.java)
        firestoreDB = FirebaseFirestore.getInstance()
        mWordViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        try {
//            activity!!.itemcard.visibility = View.GONE
//
//            activity!!.main.visibility = View.GONE
//            activity!!.sub.visibility = View.VISIBLE
            activity!!.equal_navigation_bars.visibility = View.GONE
//            activity!!.head.text="Support"
        } catch (e: Exception) {
        }

        try {
            val articleParams = HashMap<String, String>()
            //param keys and values have to be of String type
            val firstName = SessionMaintainence.instance!!.firstName
        } catch (e: Exception) {
        }
        if (sessionMaintainence!!.userType != "user") {
            floatingActionButton.setBackgroundResource(R.drawable.paymentb)
            floatingActionButton2.setBackgroundResource(R.drawable.paymentb)
//            floatingActionButton.backgroundTintList =
//                ColorStateList.valueOf(resources.getColor(R.color.colorErase))
//            floatingActionButton2.backgroundTintList =
//                ColorStateList.valueOf(resources.getColor(R.color.colorErase))
        }
        floatingActionButton.setOnClickListener {
            whatsapp()
        }
        floatingActionButton2.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:7810048001")
            startActivity(intent)
        }

        readTickets()
    }

    private fun readTickets() {
        firestoreDB!!.collection("support").whereEqualTo("uid", sessionMaintainence!!.Uid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    try {
                        supportList.clear()
                        supportmsgList!!.clear()
                    } catch (e: Exception) {
                    }
                    for (doc in task.result!!) {
                        val res = doc.toObject(supportmodel::class.java)
                        supportList.add(res)
                        supportmsgList = res.message
                    }
                    if (supportList.size != 0) {
                        try {
                            rex.layoutManager = LinearLayoutManager(context)
                            rex.adapter = supportAdapter(supportList, context!!, supportmsgList)
                        } catch (e: Exception) {
                        }
                    } else {
                        try {
                            rex.visibility = View.GONE
                            imageView35.visibility = View.VISIBLE
                        } catch (e: Exception) {
                        }
                    }
                }
            }
    }

    private fun createTicket() {
//        val dialogw = Dialog(context!!)
        val dialogw = Dialog(context!!, R.style.DialogTheme)
        dialogw.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogw.setContentView(R.layout.create_ticket)
        dialogw.setTitle("My Custom Dialog")
//        if (sessionMaintainence!!.userType != "user") {
//            dialogw.sss.setBackgroundResource(R.drawable.tradertoolbar)
//            dialogw.subs.setBackgroundResource(R.drawable.trere)
//        }
//        dialogw.closere.setOnClickListener {
//            dialogw.dismiss()
//        }
        dialogw.screnn.setOnClickListener {
            des = dialogw.desd.text.toString()
            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    context!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
                selectImageInAlbum()
                dialogw.dismiss()
            }
        }
        dialogw.subs.setOnClickListener {
            val elements = elements()
            if (elements.isNetworkAvailable(context!!)) {
                val s = dialogw.desd.text.toString()
                val sw = checkValidation(s, imagePath)
                if (sw) {

                } else {
                    context!!.toast("Please add description")
                }
            } else {
                activity!!.alert("Please connect your network") {
                    title = "Network Issue"
                    okButton {
                        it.dismiss()
                    }
                }.show()
            }


        }
        mWordViewModel!!.image.observe(this, androidx.lifecycle.Observer {
            //            dialogw.screnn.setImageBitmap(bitmap)
            Glide.with(this)
                .load(it)
                .into(dialogw.screnn)
        })
        dialogw.show()

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
                    openAlbum()
                } else {
                    Toast.makeText(context, "You deied the permission", Toast.LENGTH_SHORT).show()
                }
        }
    }

    //    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            FINAL_TAKE_PHOTO ->
//                if (resultCode == Activity.RESULT_OK) {
////                    bitmap = BitmapFactory.decodeStream(activity!!.contentResolver.openInputStream(imageUri!!))
////                    setimg!!.setImageBitmap(bitmap)
//                }
//            FINAL_CHOOSE_PHOTO ->
//                if (resultCode == Activity.RESULT_OK) {
////                    if (Build.VERSION.SDK_INT >= 19) {
//                        handleImageOnKitkat(data)
////                    } else {
////                        handleImageBeforeKitkat(data)
////                    }
//                }
//        }
//    }
    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent,OPEN_DOCUMENT_CODE)
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun handleImageOnKitkat(data: Intent?, bitmap: Bitmap) {
        val uri = data!!.data
        if (DocumentsContract.isDocumentUri(context, uri)) {
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
            imagePath = uri.path
        }
        mWordViewModel!!.insertimage(imagePath!!)
        val dialogw = Dialog(context!!, R.style.DialogTheme)
        dialogw.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogw.setContentView(R.layout.create_ticket)
        dialogw.setTitle("My Custom Dialog")
//        if (sessionMaintainence!!.userType != "user") {
//            dialogw.sss.setBackgroundResource(R.drawable.tradertoolbar)
//            dialogw.subs.setBackgroundResource(R.drawable.trere)
//        }
//        dialogw.closere.setOnClickListener {
//            dialogw.dismiss()
//        }
        dialogw.screnn.setOnClickListener {
            des = dialogw.desd.text.toString()
            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    context!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
                selectImageInAlbum()
                dialogw.dismiss()
            }
        }
        dialogw.subs.setOnClickListener {
            val elements = elements()
            if (elements.isNetworkAvailable(context!!)) {
                val s = dialogw.desd.text.toString()
                val sw = checkValidation(s, imagePath)
                if (sw) {
                    submitDatabase(s,bitmap,dialogw)
                } else {
                    context!!.toast("Please add description")
                }
            } else {
                activity!!.alert("Please connect your network") {
                    title = "Network Issue"
                    okButton {
                        it.dismiss()
                    }
                }.show()
            }


        }
        mWordViewModel!!.image.observe(this, androidx.lifecycle.Observer {
            //            dialogw.screnn.setImageBitmap(bitmap)
            this.bitmap = BitmapFactory.decodeFile(it)

            Glide.with(this)
                .load(bitmap)
                .into(dialogw.screnn)
        })
        dialogw.show()


    }

    private fun submitDatabase(s: String, imagePath: Bitmap, dialogw: Dialog) {
        progress = ProgressDialog(context)
        progress!!.setMessage("processing ..")
        progress!!.setCancelable(false)
        progress!!.show()
        val tr = supportmessagemodel(
            Timestamp.now(),
            s,
            sessionMaintainence!!.Uid!!,
            "text",
            sessionMaintainence!!.userType!!
        )
        val docc = sessionMaintainence!!.Uid!! + Timestamp.now().toString()
        val op = supportmodel(
            docc,
            "open",
            s,
            sessionMaintainence!!.Uid!!,
            Timestamp.now(),
            arrayListOf(tr),
            sessionMaintainence!!.userType!!
        )
        val privateDataRef = firestoreDB!!.collection("support").document(docc)
        privateDataRef
            .set(op)
            .addOnSuccessListener {
                supportList.clear()
                progress!!.dismiss()
                dialogw.dismiss()
                photouploadFile(imagePath!!, "supportImage", sessionMaintainence!!.Uid!!, docc)
                return@addOnSuccessListener
            }
            .addOnFailureListener { e ->
                progress!!.dismiss()
                return@addOnFailureListener
            }
    }

    private fun addImage(imageLinks: String, id: String) {
        progress = ProgressDialog(context!!)
        progress!!.setMessage("processing..")
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
                reload()
            }
            .addOnFailureListener {
                progress!!.dismiss()

            }

    }


    private fun checkValidation(s: String, imagePath: String?): Boolean {
        return s != "" && imagePath != ""
    }

    private fun handleImageBeforeKitkat(data: Intent?) {}
    private fun imagePath(uri: Uri?, selection: String?): String {
        var path: String? = null
        val cursor = activity!!.contentResolver.query(uri!!, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path!!
    }

    private fun photouploadFile(
        bitmap: Bitmap,
        folder: String,
        userId: String,
        docc: String
    ) {
        progress = ProgressDialog(context!!)
        progress!!.setMessage("processing..")
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
                addImage(imageLinks, docc)
            }
        }


    }

    override fun onStart() {
        super.onStart()
//        try {
//            supportList.clear()
//            supportmsgList!!.clear()
//        } catch (e: Exception) {
//        }
//        if (postmypostList.size == 0 && acceptmypostList.size == 0) {
//            reload()
//        }
    }

    fun reload() {
        try {
            supportList!!.clear()
            supportmsgList!!.clear()
        } catch (e: Exception) {
        }
        val frg: Fragment? = activity!!.supportFragmentManager.findFragmentByTag("support")
        val ft = activity!!.supportFragmentManager.beginTransaction()
        ft.detach(frg!!)
        ft.attach(frg)
        ft.commit()
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                // this is the image selected by the user
                imageUri = resultData.data
                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri)
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
    fun whatsapp()
    {
        val url = "https://api.whatsapp.com/send?phone=$7810048001"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}
