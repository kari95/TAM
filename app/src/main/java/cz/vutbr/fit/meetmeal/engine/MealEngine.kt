package cz.vutbr.fit.meetmeal.engine

import com.google.firebase.*
import com.google.firebase.firestore.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.*
import java.lang.RuntimeException

class MealEngine {

  private val collection = "meal"
  private var db = FirebaseFirestore.getInstance()

  fun add(meal: Meal): Completable = Completable.create { singleSubscriber ->
    db.collection(collection).add(meal).addOnCompleteListener { task ->
      if (task.isSuccessful) {
        singleSubscriber.onComplete()
      } else {
        singleSubscriber.onError(task.exception ?: Exception())
      }
    }
  }

  fun join(user: User, meal: Meal): Completable = Completable.create { singleSubscriber ->
    db.collection(collection)
      .document(meal.id)
      .update("joinedUsers", FieldValue.arrayUnion(user.id))
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          singleSubscriber.onComplete()
        } else {
          singleSubscriber.onError(task.exception ?: Exception())
        }
      }
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
                meal.id = document.id
                list.add(meal)
              } catch (e: RuntimeException) {
                e
              }
            }
            singleSubscriber.onNext(list)
            singleSubscriber.onComplete()
          } else {
            singleSubscriber.onError(Exception("no data"))
          }
        }
    }
  }

  fun find(id: String): Observable<Meal> {
    return Observable.create { singleSubscriber ->
      db.collection(collection)
        .document(id)
        .get()
        .addOnCompleteListener { task ->
          if (task.isSuccessful) {
            val result = task.result!!
            try {
              val meal = result.toObject(Meal::class.java)
              meal?.id = result.id
              if (meal == null) {
                singleSubscriber.onError(Exception("parse error"))
              } else {
                singleSubscriber.onNext(meal)
                singleSubscriber.onComplete()
              }
            } catch (e: RuntimeException) {
              singleSubscriber.onError(Exception("parse error"))
            }
          } else {
            singleSubscriber.onError(Exception("no data"))
          }
        }
    }
  }
}