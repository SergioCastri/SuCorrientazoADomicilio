package co.com.corrientazoadomicilio.servicios

import co.com.corrientazoadomicilio.entidades._

sealed trait algebraHacerEntrega{
 // def leer(ruta: String): List[Char]
    def recorrerPorInstruccion(mov : Movimiento): Posicion
  //def recorrerPorEntrega()
}

sealed trait interpreteHacerEntrega extends algebraHacerEntrega{

  def recorrerPorInstruccion(mov : Movimiento): Posicion = {
    val res: Posicion = mov.ins match{
      case  A() => interpreteDeAlgebraSeguirRuta.avanzar(mov.pos)
      case  D() => interpreteDeAlgebraSeguirRuta.moverDerecha(mov.pos)
      case  I() => interpreteDeAlgebraSeguirRuta.moverIzquierda(mov.pos)
    }
    res
  }

}
object interpreteHacerEntrega extends interpreteHacerEntrega