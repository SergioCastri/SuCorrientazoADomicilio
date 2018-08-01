package co.com.corrientazoadomicilio.servicios

import java.util.concurrent.Executors

import co.com.corrientazoadomicilio.entidades.Dron
import co.com.corrientazoadomicilio.vo.Cardinalidad.{E, N, O, S}
import co.com.corrientazoadomicilio.vo._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try



sealed trait algebraHacerEntrega{
  def recorrerPorInstruccion(dron:Dron, instruccion: Instruccion): Future[Dron]
  def seguirEntrega(entrega :Entrega, dron: Dron):Future[Dron]
  def seguirRuta(ruta :Ruta, dron: Dron):List[Future[Dron]]
}

sealed trait interpreteHacerEntrega extends algebraHacerEntrega{
  implicit val context = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

  def recorrerPorInstruccion(dron:Dron, instruccion: Instruccion): Future[Dron] = {
    val res: Future[Dron] = instruccion match{
      case  A() => interpreteDeAlgebraSeguirRuta.avanzar(dron)
      case  D() => interpreteDeAlgebraSeguirRuta.moverDerecha(dron)
      case  I() => interpreteDeAlgebraSeguirRuta.moverIzquierda(dron)
    }
    res
  }

  def seguirEntrega(entrega :Entrega, dron: Dron):Future[Dron]= {
    val l1 = Future { dron }(context)
    val lfuturos: List[Future[Dron]] = List(l1)
    val r: List[Future[Dron]] = entrega.entrega.foldLeft(lfuturos){ (resultado, item) =>
        resultado :+ resultado.last.flatMap(x => recorrerPorInstruccion(x, item))
    }
    val dronFinal = r.last
    dronFinal
  }


  def seguirRuta(ruta :Ruta, dron: Dron):List[Future[Dron]]= {
    val l1 = Future { dron }(context)
    val lfuturos: List[Future[Dron]] = List(l1)
    val res: List[Future[Dron]] = ruta.ruta.foldLeft(lfuturos){(resultado,item) =>
      resultado :+ resultado.last.flatMap(x => seguirEntrega(item, x))
    }
    res.tail
  }

}

object interpreteHacerEntrega extends interpreteHacerEntrega



//----------------------------------

sealed trait algebraSeguirRuta{

  def moverDerecha(dron: Dron):Future[Dron]
  def moverIzquierda(dron: Dron):Future[Dron]
  def avanzar(dron: Dron):Future[Dron]


}

sealed trait interpreteDeAlgebraSeguirRuta extends algebraSeguirRuta{

  implicit val context = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))

   def moverDerecha(dron: Dron): Future[Dron] = {
     val md = Future {
       val res  = dron.posicion.car match{
         case  N() =>  E()
         case  E() =>  S()
         case  S() =>  O()
         case  O() =>  N()
       }
       val respuesta = Dron(dron.capacidad, Posicion(dron.posicion.coor, res), dron.identidad)
       respuesta
     }(context)
     md
   }

   def moverIzquierda(dron: Dron): Future[Dron]= {
     val mi = Future {
       val res  = dron.posicion.car match{
         case  N() =>  O()
         case  E() =>  N()
         case  S() =>  E()
         case  O() =>  S()
       }
       val respuesta = Dron(dron.capacidad,Posicion(dron.posicion.coor, res), dron.identidad)
       respuesta
     }(context)
     mi
   }

   def avanzar(dron: Dron): Future[Dron] = {
     val aa = Future {
       val res = dron.posicion.car match {
         case N() => Coordenada(dron.posicion.coor.a, dron.posicion.coor.b +1)
         case S() => Coordenada(dron.posicion.coor.a, dron.posicion.coor.b -1)
         case E() => Coordenada(dron.posicion.coor.a +1, dron.posicion.coor.b)
         case O() => Coordenada(dron.posicion.coor.a -1, dron.posicion.coor.b)
       }
       val respuesta = Dron(dron.capacidad, Posicion(res, dron.posicion.car), dron.identidad)
       respuesta

     }(context)
     aa
   }


}

object interpreteDeAlgebraSeguirRuta extends interpreteDeAlgebraSeguirRuta