package com.ceseagod.rajarani.mainfolder

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ceseagod.rajarani.R
import com.ceseagod.rajarani.model.CateModel
import com.ceseagod.rajarani.model.Cateitemmodel
import com.ceseagod.showcase.utilities.SessionMaintainence
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_shop_item_edit.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream

class ShopItemEditActivity : AppCompatActivity() {
    var imageLinkss: String = ""
    var firestoreDB: FirebaseFirestore? = null
    private val PICK_IMAGE = 100
    private val PICK_IMAGEs = 120
    private var imageUri: Uri? = null
    var imagePath: String? = ""
    var progress: ProgressDialog? = null
    var bitmap: Bitmap? = null
    var bitmaps: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item_edit)
        val bundle = intent.getBundleExtra("myBundle")
        var person = bundle.getParcelable<CateModel>("selected_person") as Cateitemmodel

        inputs1.setText(person.title)

//        inputs2.setText(person.phoneno)
//        inputs3.setText(person.address)

        inputs4.setText(person.color_code)
        inputs5.setText(person.stack_count)
        inputs2.setText(person.posted_by)
        inputs7.setText(person.price)

//        inputs5.setText(person.starttime)
//        inputs6.setText(person.endtime)

        Glide.with(this)
            .load(person.image)
            .into(kadaiimages)

//        Glide.with(this)
//            .load(person.kadaicoverImage)
//            .into(kadaiimages)
//        imageLinks = person.kadaiImage
//        imageLinkss = person.kadaicoverImage

        placeo.setOnClickListener {
            addCate(
                inputs1.text.toString(), inputs2.text.toString(),
                inputs4.text.toString(),
                imageLinkss,person.id
            )
        }

        kadaiimages.setOnClickListener {
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
                openGallery()
            }

        }
        payimages.setOnClickListener {
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
                openGallerys()
            }

        }
    }

    private fun openGallery() {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    private fun openGallerys() {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGEs)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data!!.data
            imagePath = imageUri.toString()
            kadaiimages!!.setImageURI(imageUri)
            bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            photouploadFile()
        } else if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGEs) {
            imageUri = data!!.data
            imagePath = imageUri.toString()
            payimages!!.setImageURI(imageUri)
            bitmaps =
                MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            photoCover()
        }
    }


    private fun photouploadFile() {
        progress = ProgressDialog(this)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("kadai_item")
        val mountainImagesRef =
            storageRef.child(
                "folder" + "/" + SessionMaintainence.instance!!.Uid + Timestamp.now()
                    .toString() + ".jpg"
            )
        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            progress!!.dismiss()
        }.addOnSuccessListener { taskSnapshot ->
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                imageLinkss = ""
                imageLinkss = it.toString()
                progress!!.dismiss()

            }
        }
    }

    private fun photoCover() {
        progress = ProgressDialog(this)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("kadai_item")
        val mountainImagesRef =
            storageRef.child(
                "folder" + "/" + SessionMaintainence.instance!!.Uid + Timestamp.now()
                    .toString() + ".jpg"
            )
        val baos = ByteArrayOutputStream()
        bitmaps!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            progress!!.dismiss()
        }.addOnSuccessListener { taskSnapshot ->
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
//                imageLinks = ""
//                imageLinks = it.toString()
                progress!!.dismiss()

//                photoCover(input1, input5, input2, input3, input4, input6)
//                addCate(input1, input2, input3, input4, input5, input6, image1, imageLinkss)
//                    imageLinks,
//                    topti,
//                    toptitwo,
//                    des, input4, input6
//                )
            }
        }
    }

    private fun addCate(
        shopname: String,
        shopphone: String,
        shopaddress: String,
        shoptype: String,
        shopopen: String
    ) {

        val instance = SessionMaintainence.instance!!
        val ifd = instance.Uid!! + Timestamp.now()
//        val model =
//            CateModel(
//                shopclose,
//                SessionMaintainence.instance!!.Uid + Timestamp.now(),
//                ""
//            )
        firestoreDB = FirebaseFirestore.getInstance()
        val doc = firestoreDB!!.collection("categories").document(shopopen)
        doc.update(
            mapOf(
                "title" to shopname,
                "type" to shopaddress,
                "image" to imageLinkss,
                "posted_by" to shopphone,
                "type" to shoptype
            )
        )
            .addOnSuccessListener {
                startActivity<AdminsActivity>()
            }
            .addOnFailureListener {
                toast("fail")

            }


    }

}