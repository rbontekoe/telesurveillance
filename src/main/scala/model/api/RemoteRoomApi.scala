package model.api

import java.awt.image.BufferedImage
import java.io.File

import javax.imageio.ImageIO
import model.CompareImages
import model.domain.RoomType

object RemoteRoomApi {

  def apply() =
    new RemoteRoomApi
}

class RemoteRoomApi {

  def cleanupFolder = {
    println("Cleanup folder")
    val directory = new File(".\\pictures") // Be aware "./pictures" in Linux
    val allFiles = directory.listFiles()
    allFiles.foreach(p => if (p.getName.endsWith(".jpg")) p.delete)
    
  } // defined Cleanup folder with pictures

  
  def takePicture = {
    println("Taking picture")
    // Run Python program on Raspberry Pi in Docker container, uncomment next statement
    //"python3 takepic.py !" 
    Thread.sleep(3000)
    
  } // defined TakePicture

  def compareImages(imageNew: File, imageOld: File): Double = {
    val difference: Double = (new CompareImages).compareImages(imageNew, imageOld)

    // Save image local
    val photoNew: BufferedImage = ImageIO.read(imageNew)
    ImageIO.write(photoNew, "jpg", new File("pictures/photo2.jpg"))

    difference

  } // defined compare images (Java) - partially a simulation

}

