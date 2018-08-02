package co.com.corrientazoadomicilio
import co.com.corrientazoadomicilio.servicios.InterpreteAlgebraArchivo


object Main {
  def main(args: Array[String]): Unit = {
    val lista = List("in1.txt","in2.txt", "in3.txt", "in4.txt", "in5.txt", "in6.txt")
    val ruta = InterpreteAlgebraArchivo.escribirArchivo(lista)
  }
}
