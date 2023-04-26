package com.example.healthExpert.view.heart

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import androidx.lifecycle.Observer
import com.example.healthExpert.R
import com.example.healthExpert.compatActivity.HeartRateCompatActivity
import com.example.healthExpert.databinding.ActivityHeartRecordBinding
import com.example.healthExpert.utils.SnackbarUtil
import java.util.*
import kotlin.math.roundToInt

class HeartRecord : HeartRateCompatActivity() {
    private lateinit var binding: ActivityHeartRecordBinding

    lateinit var captureRequest: CaptureRequest.Builder
    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var cameraCaptureSession: CameraCaptureSession
    lateinit var cameraDevice: CameraDevice

    private lateinit var imageReader: ImageReader

    private val listFrame = mutableListOf<IntArray>()
    private var previousPixels: IntArray? = null
    private var numCaptures = 0
    private var mNumBeats = 0
    var hrtratebpm = 0
    private var mCurrentRollingAverage = 0
    private var mLastRollingAverage = 0
    private var mLastLastRollingAverage = 0
    private val mTimeArray = LongArray(99999)


    private var surfaceTextureAvailable = false

    companion object {
        fun startFn(context: Context) {
            val intent =
                Intent(context, HeartRecord::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeartRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        heartRateViewModel.requestStatus.observe(this, Observer { code ->
            // Update the UI based on the value of MutableLiveData
            if (code == 200){
                finish()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }else{
                SnackbarUtil.buildTesting(binding.root,code)
            }
        })


        heartRateViewModel.bpm.observe(this, Observer { item ->
            // Update the UI based on the value of MutableLiveData
            if (item != null) {
                binding.bpm.text = item
            }
        })


        getPermissions()

        binding.saveBtn.setOnClickListener {
            heartRateViewModel.addHeartRate(hrtratebpm.toString())
        }

        binding.deleteBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.backBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }


        textureView = findViewById(R.id.texture_view)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler((handlerThread).looper)

        textureView.surfaceTextureListener = object: TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                surfaceTextureAvailable = true
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                val bmp = textureView.bitmap
                val width = bmp!!.width
                val height = bmp.height
                val pixels = IntArray(height * width)

                // 计算当前帧数的红色通道总和
                bmp.getPixels(pixels, 0, width, width / 2, height / 2, width / 15, height / 15)
                var sum = 0
                for (i in 0 until height * width) {
                    val red = pixels[i] shr 16 and 0xFF
                    sum = sum + red
                }
                // 计算红色通道平均值
                if (numCaptures == 30) {
                    mCurrentRollingAverage = sum // sum/1 第一帧
                } else if (numCaptures > 30 && numCaptures < 99) {
                    mCurrentRollingAverage =
                        (mCurrentRollingAverage * (numCaptures - 30) + sum) / (numCaptures - 29)
                } else if (numCaptures >= 99) {
                    mCurrentRollingAverage = (mCurrentRollingAverage * 69 + sum) / 70
                    // 判断是否有一次心跳
                    if (mLastRollingAverage > mCurrentRollingAverage && mLastRollingAverage > mLastLastRollingAverage && mNumBeats < 15) {
                        mTimeArray[mNumBeats] = System.currentTimeMillis()
                        mNumBeats++
                        val rate = mNumBeats.div(15f).times(100)
                        heartRateViewModel.setBpm(String.format("%.0f",rate)+" %")
                        if (mNumBeats == 15) {
                            calcBPM()
                        }
                    }
                }
                numCaptures++
                mLastLastRollingAverage = mLastRollingAverage
                mLastRollingAverage = mCurrentRollingAverage
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        cameraDevice.close()
        handler.removeCallbacksAndMessages(null)
        handlerThread.quitSafely()
    }

    @SuppressLint("MissingPermission")
    fun openCamera(){
        cameraManager.openCamera(cameraManager.cameraIdList[0],object : CameraDevice.StateCallback(){
            override fun onOpened(p0: CameraDevice) {
                cameraDevice = p0

                val characteristics = cameraManager.getCameraCharacteristics(cameraDevice.id)
                val streamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                val previewSizes = streamConfigurationMap?.getOutputSizes(SurfaceTexture::class.java)
                val previewSize = previewSizes?.firstOrNull() ?: Size(800, 800) // Default size if none available

                imageReader = ImageReader.newInstance(previewSize.height, previewSize.height, ImageFormat.YUV_420_888, 1)

                // Configure the output surface to receive the preview frames
                captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                var surface = Surface(textureView.surfaceTexture)
                captureRequest.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH) // turn on Flash light
                captureRequest.addTarget(surface)
                captureRequest.addTarget(imageReader.surface)

                val outputSurfaces = listOf(surface, imageReader.surface)

                cameraDevice.createCaptureSession(outputSurfaces,object : CameraCaptureSession.StateCallback(){
                    override fun onConfigured(p0: CameraCaptureSession) {
                        cameraCaptureSession = p0
                        cameraCaptureSession.setRepeatingRequest(captureRequest.build(),null,null)
                    }
                    override fun onConfigureFailed(p0: CameraCaptureSession) {}
                },handler)

                imageReader.setOnImageAvailableListener({
                    val image = it.acquireLatestImage()
                    image.close()
                }, null)
            }
            override fun onDisconnected(p0: CameraDevice) {}
            override fun onError(p0: CameraDevice, p1: Int) {}
        },handler)
    }

    private fun calcBPM() {
        val med: Int
        val timedist = LongArray(15)
        for (i in 0..14) {
            timedist[i] = mTimeArray[i + 1] - mTimeArray[i]
        }
        Arrays.sort(timedist)
        med = timedist[timedist.size / 2].toInt()
        hrtratebpm = 60000 / med
        if(hrtratebpm>200){
            heartRateViewModel.setBpm("Detection failed, please try again")
            binding.saveBtn.visibility = View.GONE
            binding.deleteBtn.visibility = View.VISIBLE
        }else{
            heartRateViewModel.setBpm("$hrtratebpm BPM")
            binding.saveBtn.visibility = View.VISIBLE
            binding.deleteBtn.visibility = View.VISIBLE
        }
    }

    private fun processImage(image: Image) {
        val cropSize = 800

        // 计算需要截取的区域左上角坐标
        val x = (image.width - cropSize) / 2
        val y = (image.height - cropSize) / 2

        Log.d("测试", "width: ${image.width}")
        Log.d("测试", "height: ${image.height}")

        // 获取 Y 数据
        val yBuffer = image.planes[0].buffer

        // 创建 byte 数组保存 Y 数据
        val yBytes = ByteArray(yBuffer.remaining())

        // 将 Y 数据存储到 byte 数组中
        yBuffer.get(yBytes)

        // 将 Y 数据存储到一维的 byte 数组中
        val ySize = image.width * image.height
        val yuvPixels = ByteArray(ySize)
        System.arraycopy(yBytes, 0, yuvPixels, 0, ySize)

        // 截取 Y 数据中心800x800像素
        val croppedYuvPixels = ByteArray(cropSize * cropSize)
        for (i in 0 until cropSize) {
            System.arraycopy(yuvPixels, (y + i) * image.width + x, croppedYuvPixels, i * cropSize, cropSize)
        }

        // 将 Y 数据转换为灰度图像的像素数据
        val grayPixels = IntArray(cropSize * cropSize)
        for (i in 0 until croppedYuvPixels.size) {
            val gray = croppedYuvPixels[i].toInt() and 0xff
            grayPixels[i] = Color.rgb(gray, gray, gray)
        }


        val currentFrame = grayPixels

        if (listFrame.isNotEmpty()){
            val previousFrame = listFrame.last()

            var diff = calculateFrameDifferences(currentFrame,previousFrame)

            listFrame.removeLast()

            val peak = findPeaks(diff)
//            Log.d("测试", "peak: $peak")

            val avgDistance = calculateAveragePeriod(peak)

            val heartRate = calculateHeartRate(avgDistance)
            heartRateViewModel.setBpm(heartRate.toString())

        }
        listFrame.add(currentFrame)
    }

    fun calculateFrameDifferences(currentFrame: IntArray, previousFrame: IntArray): IntArray {
        val differences = IntArray(currentFrame.size)

        for (i in currentFrame.indices) {
            val diff = Math.abs(currentFrame[i] - previousFrame[i])
            differences[i] = diff
        }
        return differences
    }

    fun findPeaks(signal: IntArray): List<Int> {
        val mean = signal.average()
        val std = signal.standardDeviation()
        val threshold = mean + std
        val peaks = mutableListOf<Int>()
        var i = 1
        while (i < signal.size - 1) {
            if (signal[i] > threshold && signal[i] > signal[i - 1] && signal[i] > signal[i + 1]) {
                peaks.add(i)
                i += 10 // 可以根据需要调整步长
            } else {
                i++
            }
        }
        return peaks
    }

    fun IntArray.average(): Double {
        return if (this.isEmpty()) {
            0.0
        } else {
            this.sum().toDouble() / this.size
        }
    }

    fun IntArray.standardDeviation(): Double {
        val mean = this.average()
        return Math.sqrt(this.map { Math.pow(it.toDouble() - mean, 2.0) }.sum() / this.size)
    }

    fun calculateAveragePeriod(peaks: List<Int>): Double {
        val distances = mutableListOf<Int>()
        for (i in 1 until peaks.size) {
            distances.add(peaks[i] - peaks[i-1])
        }
        return distances.average()
    }

    fun calculateHeartRate(averagePeriod: Double): Int {
        // 计算每秒钟心跳数（BPM）
        val bpm = 60 / averagePeriod

        // 将心跳数四舍五入为整数
        return if (!bpm.isNaN()) {
            val heartRate = bpm.roundToInt()
            heartRate
        }else{
            0
        }
    }




    private fun getPermissions(){
        var permissionList = mutableListOf<String>()
        if(checkSelfPermission(android.Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
            permissionList.add(android.Manifest.permission.CAMERA)
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
            permissionList.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permissionList.size > 0){
            requestPermissions(permissionList.toTypedArray(),10)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED){
                getPermissions()
            }
        }
    }




}

//        // 计算灰度图像的平均值和标准差
//        val pixels = IntArray(cropSize * cropSize)
//        bmp.getPixels(pixels, 0, cropSize, 0, 0, cropSize, cropSize)
//
//        var sum = 0.0
//        for (i in 0 until cropSize * cropSize) {
//            val gray = Color.red(pixels[i])
//            sum += gray
//        }
//        val mean = sum / (cropSize * cropSize)
//
//        var variance = 0.0
//        for (i in 0 until cropSize * cropSize) {
//            val gray = Color.red(pixels[i])
//            variance += (gray - mean) * (gray - mean)
//        }
//        variance /= (cropSize * cropSize)
//        val stdDev = Math.sqrt(variance)
//
//        Log.d("平均值", "mean: $mean ")
//        Log.d("标准差", "stdDev: $stdDev ")

// 显示灰度图像
//        runOnUiThread {
//            binding.image.setImageBitmap(bmp)
//        }