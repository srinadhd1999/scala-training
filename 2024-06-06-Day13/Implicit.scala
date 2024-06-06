
  implicit def intToString(x: Int): String = x.toString

  @main def Implicit: Unit = {
    val number: Int = 123
    val text: String = intToString(number)
    println(text)
    println(s"Tyoe of $text is ${text.getClass()}") 
  }
