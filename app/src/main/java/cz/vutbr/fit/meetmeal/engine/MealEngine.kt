package cz.vutbr.fit.meetmeal.engine

import com.google.firebase.firestore.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.*

class MealEngine {

  private val collection = "meal"
  private var db = FirebaseFirestore.getInstance()

  fun findAll(): Observable<List<Meal>> {
    return Observable.create{ singleSubscriber ->
      db.collection(collection)
        .get()
        .addOnCompleteListener { task ->
          val list = mutableListOf<Meal>()
          if (task.isSuccessful) {
            for (document in task.result!!) {
              val data = document.data
              //list.add()
            }
            singleSubscriber.onNext(list)
          } else {
            singleSubscriber.onError(Exception("no data"))
          }
        }
    }
  }
}