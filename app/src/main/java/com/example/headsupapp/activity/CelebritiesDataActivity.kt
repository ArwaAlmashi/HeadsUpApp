package com.example.headsupapp.activity

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.headsupapp.R
import com.example.headsupapp.databinding.ActivityCelebritiesDataBinding
import com.example.headsupapp.model.Celebrity
import com.example.headsupapp.model.CelebrityModel
import com.example.headsupapp.model.CelebrityModelItem
import com.example.headsupapp.recyclerview.CelebrityRvAdapter
import com.example.headsupapp.services.ApiClient
import com.example.headsupapp.services.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent


class CelebritiesDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCelebritiesDataBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var celebrityList: CelebrityModel
    private lateinit var adapter: CelebrityRvAdapter

    private val apiInterface by lazy { ApiClient().getClient().create(ApiInterface::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCelebritiesDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        celebrityList = CelebrityModel()

        recyclerView = binding.recyclerview
        adapter = CelebrityRvAdapter(this, celebrityList)
        recyclerView.adapter = adapter

        getCelebrityListFromApi()


        binding.backArrow.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        binding.addIcon.setOnClickListener {
            postAlert(this)
        }


    }

    // ----------- API FUNCTIONS -----------

    // GET API
    private fun getCelebrityListFromApi() {
        apiInterface.getApiData().enqueue(object : Callback<CelebrityModel> {
            override fun onResponse(call: Call<CelebrityModel>, response: Response<CelebrityModel>) {
                println("Success Get")
                celebrityList = response.body()!!
                adapter.updateCelebrityList(celebrityList)
                recyclerView.scheduleLayoutAnimation()

            }

            override fun onFailure(call: Call<CelebrityModel>, t: Throwable) {
                println("Failure GET: ${t.message}")
            }

        })
    }

    // POST API
    private fun postCelebrityItemInApi(celebrity: Celebrity) {
        apiInterface.postApiData(celebrity).enqueue(object : Callback<Celebrity> {
            override fun onResponse(call: Call<Celebrity>, response: Response<Celebrity>) {
                println("Success Post")
                getCelebrityListFromApi()
            }

            override fun onFailure(call: Call<Celebrity>, t: Throwable) {
                println("Failure Post: ${t.message}")
            }

        })
    }

    // UPDATE API
    private fun updateCelebrityItemInApi(celebrityItem: CelebrityModelItem, context: Context) {
        apiInterface.updateApiData(
                celebrityItem.pk,
                celebrityItem
        ).enqueue(object : Callback<CelebrityModelItem> {
            override fun onResponse(
                    call: Call<CelebrityModelItem>,
                    response: Response<CelebrityModelItem>
            ) {
                println("Success Update")
                val intent = Intent(context, CelebritiesDataActivity::class.java)
                context.startActivity(intent)
            }

            override fun onFailure(call: Call<CelebrityModelItem>, t: Throwable) {
                println("Failure Update: ${t.message}")
            }

        })
    }

    // DELETE API
    private fun deleteCelebrityItemFromApi(celebrity: CelebrityModelItem, context: Context) {
        apiInterface.deleteApiData(celebrity.pk).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                println("Success Delete ")
                val intent = Intent(context, CelebritiesDataActivity::class.java)
                context.startActivity(intent)
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Failure Delete: ${t.message}")
            }

        })

    }

    // ----------- ALERT FUNCTIONS -----------

    // Alert POST
    fun postAlert(context: Context) {

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.add_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val nameInput = dialog.findViewById<EditText>(R.id.celebrity_name_et)
        val taboo1Input = dialog.findViewById<EditText>(R.id.taboo1_et)
        val taboo2Input = dialog.findViewById<EditText>(R.id.taboo2_et)
        val taboo3Input = dialog.findViewById<EditText>(R.id.taboo3_et)
        val addCelebrityButton = dialog.findViewById<TextView>(R.id.add_new_celebrity_button)
        dialog.show()

        addCelebrityButton.setOnClickListener {
            if (nameInput.text != null || taboo1Input.text != null || taboo2Input.text != null || taboo3Input.text != null) {
                val newCelebrity = Celebrity(
                        nameInput.text.toString(),
                        taboo1Input.text.toString(),
                        taboo2Input.text.toString(),
                        taboo3Input.text.toString()
                )
                postCelebrityItemInApi(newCelebrity)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please fill all text fields", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Alert UPDATE
    fun updateAlert(context: Context, celebrity: CelebrityModelItem) {

        val dialog = Dialog(context)
        dialog.setContentView(R.layout.update_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val nameInput = dialog.findViewById<EditText>(R.id.celebrity_name_et_update)
        val taboo1Input = dialog.findViewById<EditText>(R.id.taboo1_et_update)
        val taboo2Input = dialog.findViewById<EditText>(R.id.taboo2_et_update)
        val taboo3Input = dialog.findViewById<EditText>(R.id.taboo3_et_update)
        val updatedCelebrityButton = dialog.findViewById<TextView>(R.id.update_celebrity_button)

        nameInput.setText(celebrity.name)
        taboo1Input.setText(celebrity.taboo1)
        taboo2Input.setText(celebrity.taboo2)
        taboo3Input.setText(celebrity.taboo3)

        dialog.show()

        updatedCelebrityButton.setOnClickListener {
            celebrity.name = nameInput.text.toString()
            celebrity.taboo1 = taboo1Input.text.toString()
            celebrity.taboo2 = taboo2Input.text.toString()
            celebrity.taboo3 = taboo3Input.text.toString()
            updateCelebrityItemInApi(celebrity, context)
            dialog.dismiss()
        }

    }

    // Delete
    fun delete(celebrity: CelebrityModelItem, context: Context){
        deleteCelebrityItemFromApi(celebrity, context)
    }


}