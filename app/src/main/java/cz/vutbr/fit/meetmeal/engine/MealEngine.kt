package cz.vutbr.fit.meetmeal.engine

import com.google.firebase.*
import com.google.firebase.firestore.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.*
import java.lang.RuntimeException

class MealEngine {

  private val collection = "meal"
  private var db = FirebaseFirestore.getInstance()

  fun add(meal: Meal) {
    db.collection(collection).add(meal)
  }

  fun findAll(): Observable<List<Meal>> {
    return Observable.create { singleSubscriber ->
      db.collection(collection)
        .orderBy("time")
        .whereGreaterThan("time", Timestamp.now())
        .get()
        .addOnCompleteListener { task ->
          val list = mutableListOf<Meal>()
          if (task.isSuccessful) {
            for (document in task.result!!) {
              try {
                val meal = document.toObject(Meal::class.java)
                list.add(meal)
              } catch (e: RuntimeException) {}
            }
            singleSubscriber.onNext(list)
          } else {
            singleSubscriber.onError(Exception("no data"))
          }
        }
    }
  }
}