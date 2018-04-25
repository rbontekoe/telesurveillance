package model.domain

import org.scalatest.FunSuite

class RoomTypeSpec extends FunSuite {
  test("the name should be Kitchen") {
    val r = RoomType.Kitchen
    assert(r.toString() == "Kitchen")
  }
  
 test("the name should be Bathroom") {
    val r = RoomType.Bathroom
    assert(r.toString() == "Bathroom")
  }
 
 test("the name should be Bedroom") {
    val r = RoomType.Bedroom
    assert(r.toString() == "Bedroom")
  }
 
 test("the name should be Livingroom") {
    val r = RoomType.Livingroom
    assert(r.toString() == "Livingroom")
  }
 
 test("the name should be Balcony") {
    val r = RoomType.Balcony
    assert(r.toString() == "Balcony")
  }
 
 test("the name should be Unkown") {
    val r = RoomType.Unkown
    assert(r.toString() == "Unkown")
  }
 
}

class TextToRoomTypeSpec extends FunSuite {
  
  test("the name should be Kitchen") {
    val r = RoomType.textToRoomType("kitchen")
    assert(r.toString() == "Kitchen")
  }
  
  test("the name should be Bathroom") {
    val r = RoomType.textToRoomType("bathroom")
    assert(r.toString() == "Bathroom")
  }
  
  test("the name should be Bedroom") {
    val r = RoomType.textToRoomType("bedroom")
    assert(r.toString() == "Bedroom")
  }
  
  test("the name should be Livingroom") {
    val r = RoomType.textToRoomType("livingroom")
    assert(r.toString() == "Livingroom")
  }
  
  test("the name should be Balcony") {
    val r = RoomType.textToRoomType("balcony")
    assert(r.toString() == "Balcony")
  }
  
  test("the name should be Unkown with any name") {
    val r = RoomType.textToRoomType("any name")
    assert(r.toString() == "Unkown")
  }
  
}

class AlarmTypeSpec extends FunSuite {
  
  test("the name should be Info") {
    val a = AlarmType.Info
    assert(a.toString() == "Info")
  }
  
  test("the name should be Warning") {
    val a = AlarmType.Warning
    assert(a.toString() == "Warning")
  }
  
  test("the name should be Severe") {
    val a = AlarmType.Severe
    assert(a.toString() == "Severe")
  }
  
  test("the name should be Error") {
    val a = AlarmType.Error
    assert(a.toString() == "Error")
  }
  
  test("the name should be Unkown") {
    val a = AlarmType.Unkown
    assert(a.toString() == "Unkown")
  }
}

class TextToAlarmTypeSpec extends FunSuite {
  test("the name should be Info") {
    val a = AlarmType.textToAlarmType("infO")
    assert(a.toString() == "Info")
  }
  
  test("the name should be Warning") {
    val a = AlarmType.textToAlarmType("warning")
    assert(a.toString() == "Warning")
  }
  
  test("the name should be Severe") {
    val a = AlarmType.textToAlarmType("severe")
    assert(a.toString() == "Severe")
  }
  
  test("the name should be Error") {
    val a = AlarmType.textToAlarmType("error")
    assert(a.toString() == "Error")
  }
  
  test("the name should be Unkown with infOO") {
    val a = AlarmType.textToAlarmType("infOO")
    assert(a.toString() == "Unkown")
  }
}