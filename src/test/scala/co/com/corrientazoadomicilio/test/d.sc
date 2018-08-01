import co.com.corrientazoadomicilio.vo.Instruccion

import scala.util.{Success, Try}


//CASO FELIZ
val lisIns: List[Try[Instruccion]] = List(Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('I'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('D'))
//val lisIns2 = lisIns.takeWhile(x=> x.isSuccess).map(y=>y.map(z=>)
lisIns.map(x=> x.toOption)



// caso mal
val lisInsa: List[Try[Instruccion]] = List(Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('g'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('I'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('A'),Instruccion.nuevaInstruccion('D'))
lisInsa.map(x=> x.toOption).flatten
