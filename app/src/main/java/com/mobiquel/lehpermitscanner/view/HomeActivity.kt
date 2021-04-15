package com.mobiquel.lehpermitscanner.view

import android.content.Intent
import android.net.ParseException
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.integration.android.IntentIntegrator
import com.mobiquel.lehpermitscanner.R
import com.mobiquel.lehpermitscanner.service.Supplier
import com.mobiquel.lehpermitscanner.utils.Security
import com.mobiquel.lehpermitscanner.view.adapter.CircuitListAdapter
import com.mobiquel.lehpermitscanner.view.adapter.PassengerListAdapter
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var context = this

        scan.setOnClickListener {
            IntentIntegrator(this).initiateScan()
            lot1.visibility = View.VISIBLE
            correctLayout.visibility = View.GONE
            wrongLayout.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {

                showToast("Cancelled")
            } else {

                showToast("Scanned")
                try {
                    var data = Security.decrypt(result.contents.replace(" ", "+"))
                    try {
                        Log.e("VALID DATA", "===== " + data);
                        openPermitScanResult(data)
                    } catch (e: JSONException) {
                        showToast("Invalid Permit!")
                    }
                } catch (e: java.lang.Exception) {
                    lot1.visibility=View.GONE
                    correctLayout.visibility = View.GONE
                    wrongLayout.visibility = View.VISIBLE
                    lot2.playAnimation()
                }


            }
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun openPermitScanResult(data: String) {
        lot1.visibility = View.GONE
        try {
            var jsonObject = JSONObject(data)
            var listOfAgentString: MutableList<String>? = null
            listOfAgentString = ArrayList()
            for (i in 0..jsonObject.getJSONArray("registrationList").length().minus(1)) {
                listOfAgentString.add(
                    jsonObject.getJSONArray("registrationList").getJSONObject(i).toString()
                )
            }
            val adapter = listOfAgentString?.let { PassengerListAdapter(this, it) }
            passengerList?.layoutManager = LinearLayoutManager(this)
            passengerList?.adapter = adapter
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val formatted = SimpleDateFormat("dd MMM, yyyy")
            try {
                startDate.setText(
                    formatted.format(
                        sdf.parse(
                            jsonObject.getJSONObject("permitDetails").getString("startDate")
                        )
                    )
                )
                endDate.setText(
                    formatted.format(
                        sdf.parse(
                            jsonObject.getJSONObject("permitDetails").getString("endDate")
                        )
                    )
                )

            } catch (e: ParseException) {
                e.printStackTrace()
            }

            if (listOfAgentString.size == 0) {
                correctLayout.visibility = View.GONE
                wrongLayout.visibility = View.VISIBLE
                lot2.playAnimation()
            } else {
               var jsonObjectSub:JSONObject= JSONObject(listOfAgentString.get(0))
                if(jsonObjectSub.getString("touristType").equals("Domestic"))
                {
                    var lisOfCkts=Supplier.domesticCkts
                    var ckktIds=jsonObject.getJSONObject("permitDetails").getString("circuitIds")
                    var listString:MutableList<String>?=ArrayList()
                    for(i in 0..lisOfCkts.size-1){
                            if(ckktIds.contains(lisOfCkts.get(i).id))
                                listString!!.add(lisOfCkts.get(i).name)
                    }
                    val adapter = listString?.let { CircuitListAdapter(this, it) }
                    cktList?.layoutManager = LinearLayoutManager(this)
                    cktList?.adapter = adapter

                }
                else
                {
                    var lisOfCkts=Supplier.ForeignCkts
                    var ckktIds=jsonObject.getJSONObject("permitDetails").getString("circuitIds")
                    var listString:MutableList<String>?=ArrayList()
                    for(i in 0..lisOfCkts.size-1){
                        if(ckktIds.contains(lisOfCkts.get(i).id))
                            listString!!.add(lisOfCkts.get(i).name)
                    }
                    val adapter = listString?.let { CircuitListAdapter(this, it) }
                    cktList?.layoutManager = LinearLayoutManager(this)
                    cktList?.adapter = adapter

                }
                wrongLayout.visibility = View.GONE
                correctLayout.visibility = View.VISIBLE
                lot3.playAnimation()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            correctLayout.visibility = View.GONE
            wrongLayout.visibility = View.VISIBLE
            lot2.playAnimation()
        }

    }
}