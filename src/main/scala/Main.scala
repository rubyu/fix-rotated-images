import org.opencv.core.{Core, CvType, Mat, MatOfKeyPoint, Scalar}
import org.opencv.features2d.{DescriptorExtractor, FeatureDetector, Features2d}
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.utils.Converters

object Main {

  def binaryString(bytes: Array[Byte]): String =
    bytes
      .map(b => "%8s".format(Integer.toBinaryString(b & 0xFF)).replace(" ", "0"))
      .mkString

  def hexString(bytes: Array[Byte]): String =
    bytes
      .map("%02x" format _).mkString

  def main(args: Array[String]): Unit = {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    val jpg = new java.io.File(getClass.getResource("/lena.png").getPath)
    val image = Imgcodecs.imread(jpg.getPath)
    val gray1 = new Mat(image.height(), image.width(), CvType.CV_8UC1)
    val gray2 = new Mat(image.height(), image.width(), CvType.CV_8UC1)
    Imgproc.cvtColor(image, gray1, Imgproc.COLOR_BGR2GRAY)
    Imgproc.equalizeHist(gray1, gray2)

    val detector = FeatureDetector.create(FeatureDetector.AKAZE)
    val extractor = DescriptorExtractor.create(DescriptorExtractor.AKAZE)
    val keys = new MatOfKeyPoint()
    val descriptors = new Mat(image.rows(), image.cols(), image.`type`())

    println(f"descriptor size: ${extractor.descriptorSize()}")
    println(f"descriptor type: ${extractor.descriptorType()}")

    detector.detect(gray2, keys)
    println("Detected %s key points".format(keys.toArray.size))
    extractor.compute(image, keys, descriptors)
    keys.toArray.zipWithIndex.foreach { case (key, i) =>
      println(f"x=${key.pt.x}, y=${key.pt.y}, r=${key.response} o=${key.octave}, a=${key.angle}")
      val m = descriptors.row(i)
      val code = new Array[Byte](extractor.descriptorSize())
      m.get(0, 0, code)
      println(f"m total: ${ m.total() }")
      println(f"m channels: ${ m.channels() }")
      println(f"code size: ${ code.size }")
      println(f"descriptor size: ${ binaryString(code).size }")
      println(f"descriptor: ${ binaryString(code) }")
      println(f"descriptor: ${ hexString(code) }")
      /*
      val code = (0 until m.total().toInt).map(m.get(0, _)(0)).toList
      /*
      for (j <- m.total().toInt) {
        val d = m.get(0, j)
        print(d.toList.toString)
      }
      */
      println(f"descriptor ${ code.mk }")
      */
    }
    val result2 = new Mat(image.height(), image.width(), CvType.CV_8UC4)
    Features2d.drawKeypoints(image, keys, result2, Scalar.all(-1), Features2d.DRAW_RICH_KEYPOINTS)
    Imgcodecs.imwrite("result.jpg", result2)
  }
}
