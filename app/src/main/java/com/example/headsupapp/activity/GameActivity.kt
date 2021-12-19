package com.example.headsupapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Surface
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.headsupapp.databinding.ActivityGameBinding
import com.example.headsupapp.model.CelebrityModel
import com.example.headsupapp.services.ApiClient
import com.example.headsupapp.services.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var celebrityList: CelebrityModel
    private var progr = 0
    private var gameActive = false
    private var countInCelebrityList = 0

    private val apiInterface by lazy { ApiClient().getClient().create(ApiInterface::class.java) }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set UI
        setUI()

        // Get Api data
        getCelebrityListFromApi()

        // Buttons listener
        binding.startButton.setOnClickListener {
            startGame()
        }
        binding.startButtonLand?.setOnClickListener {
            startGame()
        }
        binding.settingIcon.setOnClickListener {
            val intent = Intent(this, CelebritiesDataActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setUI() {
        supportActionBar?.hide()
        checkPortableAndLandscapeView(true)
        updatedProgress()
    }

    // GET API data
    private fun getCelebrityListFromApi() {
        apiInterface.getApiData().enqueue(object : Callback<CelebrityModel> {
            override fun onResponse(
                call: Call<CelebrityModel>,
                response: Response<CelebrityModel>
            ) {
                println("Success Get")
                celebrityList = response.body()!!
            }

            override fun onFailure(call: Call<CelebrityModel>, t: Throwable) {
                println("Failure GET: ${t.message}")
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun startGame() {
        if (gameActive) {
            binding.startButton.text = "START"
            gameOver()
        } else {
            timer()
            binding.startButton.text = "STOP"
            binding.startButtonLand?.text = "STOP"
            binding.yellowTv?.text = "Rotate the device to get a new Celebrity"
            gameActive = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun gameOver() {
        if (gameActive) {
            // views
            binding.yellowTv?.text = "Game Over!"
            binding.celebrityNameTvLand?.text = "Game Over!"
            binding.timerNumberTv.text = "60"
            binding.timerNumberTvLand?.text = "60"
            binding.taboosTvLand?.text = ""
            // progress
            progr = 0
            updatedProgress()
            // Update List
            getCelebrityListFromApi()
            gameActive = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updatedProgress() {
        binding.progressBar?.max = 60
        binding.progressBarLand?.max = 60

        binding.progressBar?.setProgress(progr, true)
        binding.progressBarLand?.setProgress(progr, true)

    }

    // Timer
    private fun timer() {
        val timer = object : CountDownTimer(60000, 1000) {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onTick(millisUntilFinished: Long) {
                if (gameActive) {
                    binding.timerNumberTv.text = "${millisUntilFinished / 1000}"
                    binding.timerNumberTvLand?.text = "${millisUntilFinished / 1000}"
                    progr += 1
                    updatedProgress()
                } else {
                    cancel()
                }
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onFinish() {
                gameOver()
            }
        }
        timer.start()
    }




    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val rotation = windowManager.defaultDisplay.rotation
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            checkPortableAndLandscapeView(true)

        } else {
            checkPortableAndLandscapeView(false)
            val randomIndex = Random.nextInt(celebrityList.size)
            val randomCelebrity = celebrityList[randomIndex]
            celebrityList.remove(randomCelebrity)

            if (celebrityList.size != 0 && gameActive) {
                binding.celebrityNameTvLand?.text = randomCelebrity.name
                binding.taboosTvLand?.text =
                    "${randomCelebrity.taboo1}, ${randomCelebrity.taboo2}, ${randomCelebrity.taboo3}"
            } else {
                gameOver()
            }
        }
    }

    private fun checkPortableAndLandscapeView(mode: Boolean) {

        // Portable
        binding.clockContainer.isVisible = mode
        binding.grayCircle?.isVisible = mode
        binding.progressBar.isVisible = mode
        binding.yellowTv?.isVisible = mode
        binding.startButton.isVisible = mode
        binding.timerNumberTv.isVisible = mode

        // Landscape
        binding.clockContainerLand?.isVisible = !mode
        binding.grayCircleLand?.isVisible = !mode
        binding.progressBarLand?.isVisible = !mode
        binding.celebrityNameTvLand?.isVisible = !mode
        binding.taboosTvLand?.isVisible = !mode
        binding.startButtonLand?.isVisible = !mode
        binding.timerNumberTvLand?.isVisible = !mode

    }


}