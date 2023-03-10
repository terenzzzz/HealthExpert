package com.example.healthExpert.view.heart

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.media.Image
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import com.example.healthExpert.R
import com.example.healthExpert.databinding.ActivityHeartRecordBinding
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


class HeartRecord : AppCompatActivity() {
    private lateinit var binding: ActivityHeartRecordBinding

    lateinit var captureRequest: CaptureRequest.Builder
    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView
    lateinit var cameraCaptureSession: CameraCaptureSession
    lateinit var cameraDevice: CameraDevice

    private lateinit var imageReader: ImageReader

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

        getPermissions()


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
                val previewSize = previewSizes?.firstOrNull() ?: Size(640, 480) // Default size if none available

                imageReader = ImageReader.newInstance(previewSize.width, previewSize.height, ImageFormat.YUV_420_888, 1)

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

                    override fun onConfigureFailed(p0: CameraCaptureSession) {

                    }

                },handler)


                imageReader.setOnImageAvailableListener({
                    val image = it.acquireLatestImage()
                    if (image != null) {


//                        val bitmap = imageToBitmap(image)
//
//                        runOnUiThread {
//                            binding.image.setImageBitmap(bitmap)
//                        }
                        processImage(image)


//                        showBitmapOnTextureView(bitmap)
                        Log.d("帧", "bitmap: ")


                    }
                    image.close()
                }, null)
            }

            override fun onDisconnected(p0: CameraDevice) {

            }

            override fun onError(p0: CameraDevice, p1: Int) {

            }

        },handler)
    }


    private fun processImage(image: Image) {
        // 获取 Y数据
        val yBuffer = image.planes[0].buffer

        // 创建 byte 数组保存 Y数据
        val yBytes = ByteArray(yBuffer.remaining())

        // 将 Y数据存储到 byte 数组中
        yBuffer.get(yBytes)

        // 将 Y 数据存储到一维的 byte 数组中
        val ySize = image.width * image.height
        val yuvPixels = ByteArray(ySize)
        System.arraycopy(yBytes, 0, yuvPixels, 0, ySize)

        // 将 Y 数据转换为灰度图像的像素数据
        val grayPixels = IntArray(ySize)
        for (i in 0 until ySize) {
            val gray = yuvPixels[i].toInt() and 0xff
            grayPixels[i] = Color.rgb(gray, gray, gray)
        }

        // 将灰度图像的像素数据转换为 Bitmap 对象
        val bmp = Bitmap.createBitmap(grayPixels, image.width, image.height, Bitmap.Config.ARGB_8888)

        // 显示灰度图像
        runOnUiThread {
            binding.image.setImageBitmap(bmp)
        }

    }




    private fun detectHeartRate(red: Int, green: Int, blue: Int) {
        // Implement heart rate detection algorithm here
        // You can use signal processing techniques such as FFT and bandpass filtering
        // to extract the heart rate signal from the color data
        // There are also many open source heart rate detection algorithms available online
        // that you can use or modify for your specific use case
        Log.d("照片", "red: $red,green: $green,blue: $blue,")
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


//fun calculateGrayChannelAverage(bitmap: Bitmap): Double {
//    // Initialize OpenCV
//    if (!OpenCVLoader.initDebug()) {
//        // Handle initialization error
//    }
//
//    var sum = 0.0
//    val mat = Mat()
//    Utils.bitmapToMat(bitmap, mat)
//
//    // Convert the image to grayscale
//    Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY)
//
//    // Loop through each pixel in the image and add its value to the sum
//    for (i in 0 until mat.rows()) {
//        for (j in 0 until mat.cols()) {
//            val pixelValue = mat.get(i, j)[0] // Get the gray channel value
//            sum += pixelValue
//        }
//    }
//
//    // Calculate the average value
//    val totalPixels = mat.rows() * mat.cols()
//    val average = sum / totalPixels
//
//    return average
//}
//
//
//fun toGrayScale(bitmap: Bitmap): Bitmap {
//    // Initialize OpenCV
//    if (!OpenCVLoader.initDebug()) {
//        // Handle initialization error
//    }
//
//    // Convert the bitmap to a Mat object
//    val mat = Mat(bitmap!!.height, bitmap.width, CvType.CV_8UC4)
//    Utils.bitmapToMat(bitmap, mat)
//
//    // Convert the image to grayscale
//    Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY)
//
//    // Convert the Mat object back to a bitmap
//    val grayBitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
//    Utils.matToBitmap(mat, grayBitmap)
//    return grayBitmap
//}
//
//
//
//
//fun imageToBitmap(image: Image): Bitmap {
//    val width = image.width
//    val height = image.height
//
//    val yBuffer = image.planes[0].buffer
//    val uBuffer = image.planes[1].buffer
//    val vBuffer = image.planes[2].buffer
//
//    val ySize = yBuffer.remaining()
//    val uSize = uBuffer.remaining()
//    val vSize = vBuffer.remaining()
//
//    val nv21 = ByteArray(ySize + uSize + vSize)
//
//    // Copy the Y-plane buffer data to the nv21 array
//    yBuffer.get(nv21, 0, ySize)
//
//    // Copy the U and V plane buffer data to the nv21 array
//    vBuffer.get(nv21, ySize, vSize)
//    uBuffer.get(nv21, ySize + vSize, uSize)
//
//    // Create a YuvImage object from the nv21 array
//    val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
//
//    // Convert the YuvImage object to a Bitmap object
//    val out = ByteArrayOutputStream()
//    yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
//    val jpegArray = out.toByteArray()
//    return BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.size)
//}
//
//
//
//private fun showBitmapOnTextureView(bitmap: Bitmap) {
//    textureView.surfaceTexture?.let { surfaceTexture ->
//        val surface = Surface(surfaceTexture)
//        if (surfaceTextureAvailable){
//            val canvas = surface.lockCanvas(null)
//            canvas.drawBitmap(bitmap, 0f, 0f, null)
//            surface.unlockCanvasAndPost(canvas)
//        }
//
//    }
//}


// 计算当前帧率
//val FRAME_COUNT_INTERVAL = 60
//    val NANO_IN_ONE_SECOND = 1000000000L
//    var frameCount = 0
//    var lastTimestamp: Long = 0
//
//
//    val captureCallback = object : CameraCaptureSession.CaptureCallback() {
//        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
//            val timestamp = System.nanoTime()
//
//            if (lastTimestamp == 0L) {
//                lastTimestamp = timestamp
//            }
//
//            frameCount++
//
//            if (frameCount == FRAME_COUNT_INTERVAL) {
//                val elapsedTime = timestamp - lastTimestamp
//                val fps = FRAME_COUNT_INTERVAL.toDouble() * NANO_IN_ONE_SECOND / elapsedTime
//
//                Log.d("帧率", "FPS: $fps")
//
//                lastTimestamp = timestamp
//                frameCount = 0
//            }
//        }
//    }