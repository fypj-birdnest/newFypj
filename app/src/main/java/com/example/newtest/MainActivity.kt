package com.example.newtest

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.newtest.Class.QrString

import com.example.newtest.Class.User
import com.google.firebase.database.*
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler



class MainActivity : AppCompatActivity(),ResultHandler {
    private val REQUEST_CAMERA = 1
    private var scannerView :ZXingScannerView? = null
    lateinit var ref : DatabaseReference
    private lateinit var database: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scannerView = ZXingScannerView(this)
        setContentView(scannerView)


        // this is the code for reading data
        val ref = FirebaseDatabase.getInstance().getReference("qr")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("failed","dead")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    for(h in p0.children){
                        val hero = h.getValue(QrString::class.java)?.tvalue
                        Log.d("test hero", "$hero")
                    }
                }
            }
        })


        if(!checkPermission())
            requestPermission()


    }


    private fun checkPermission() : Boolean{
        return ContextCompat.checkSelfPermission(this@MainActivity,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),REQUEST_CAMERA)
    }

    override fun onResume(){
        super.onResume()

        if(checkPermission()){
            if(scannerView == null){
                scannerView = ZXingScannerView(this)
                setContentView(scannerView)
            }
            scannerView?.setResultHandler(this)
            scannerView?.startCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scannerView?.stopCamera()
    }

    override fun handleResult(p0: Result?) {
        var tmsg:String? = ""
        var check:Boolean? = false
        val result = p0?.text

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Result")
        builder.setPositiveButton("OK") {dialog, which ->
            scannerView?.resumeCameraPreview(this@MainActivity)
            startActivity(intent)
        }



        val ref = FirebaseDatabase.getInstance().getReference("qr")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Log.d("failed","dead")
            }

            override fun onDataChange(p1: DataSnapshot) {
                if(p1!!.exists()){
                    for(h in p1.children){
                        val hero = h.getValue(QrString::class.java)?.tvalue
                        Log.d("test hero", "$hero")
                        if(result == hero){
                            check = true
                        }
                    }
                }
                if(check == true){
                    trueQr()
                }else{
                    falseQr()
                }

            }
            fun trueQr(){
                tmsg = "The QR Code exist in database"
                builder.setMessage(tmsg)
                val alert = builder.create()
                alert.show()
            }
            fun falseQr(){
                tmsg = "The QR Code is fake"
                builder.setMessage(tmsg)
                val alert = builder.create()
                alert.show()
            }
        })








    }
}
