package co.com.corrientazoadomicilio.entidades

case class Dron(capacidad: Int, posicion : Posicion, identidad:Int)
case class Coordenada(a: Int, b: Int)
case class Entrega(entrega: List[Posicion])
case class Ruta(ruta: List[Entrega])
case class Posicion(coor: Coordenada, car: Cardinalidad)


//case class Ruta(posicion: Posicion)


/*
object Posicion {
  def apply(coordenada: Either[String, Coordenada], car: Cardinalidad): Either[String, Coordenada] ={
    coordenada.fold[Coordenada](s => {
      "Se salió del barrio"
    },
      i=> {
        Coordenada(c)
      }
    )
  }
}*/