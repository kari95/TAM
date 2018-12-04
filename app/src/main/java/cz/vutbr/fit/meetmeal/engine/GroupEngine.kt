package cz.vutbr.fit.meetmeal.engine

import com.google.firebase.firestore.*
import cz.vutbr.fit.meetmeal.model.*
import io.reactivex.*

class GroupEngine {
  private val collection = "group"
  private var db = FirebaseFirestore.getInstance()

  fun add(group: Group) {
    db.collection(collection).add(group)
  }

  fun findAll(): Observable<List<Group>> {
    return Observable.create { singleSubscriber ->
      db.collection(collection)
        .get()
        .addOnCompleteListener { task ->
          val list = mutableListOf<Group>()
          if (task.isSuccessful) {
            for (document in task.result!!) {
              val group = document.toObject(Group::class.java)
              list.add(group)
            }
            singleSubscriber.onNext(list)
            singleSubscriber.onComplete()
          } else {
            singleSubscriber.onError(Exception("no data"))
          }
        }
    }
  }
}