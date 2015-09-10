(ns
  ^{:author xlo}
  control.UserManagerLogic
  (:import [model.db UserCollection BlogCollection MarkUserCollection MessageCollection]
           [control ManagerLogic]
           [org.bson Document]
           [config LengthLimitConfig]))

(defn loginUser [username password]
  (if (or (nil? username) (nil? password)) false
    (let [data (. (new UserCollection) getUserData username)]
      (if (nil? data) (do (println "not found") false)
        (let [doucment (. data object)]
          (. (. doucment get "password") equals password))))))

(defn register [username password]
  (if (or (nil? username) (nil? password)) false
    (let [lengthLimitConfig (. LengthLimitConfig getConfig)]
      (if
        (or (> (count username) (. lengthLimitConfig getLimit "username"))
          (> (count password) (. lengthLimitConfig getLimit "password"))) false
        (let [userCollection (new UserCollection)]
          (. userCollection lockCollection)
          (if-not (nil? (. userCollection getUserData username)) false
            (do (. userCollection registerUser username password) true)))))))

(defn removeUser [username password]
  (let [userCollection (new UserCollection)]
    (. userCollection removeUser username))
  (let [blogCollection (new BlogCollection)]
    (let [blogs (vec (. blogCollection findDocumentListData (new Document "author" username)))]
      (dotimes [i (count blogs)] (. blogCollection removeDocument (. (. (. (nth blogs i) object) get "_id") toString)))))
  (let [markUserCollection (new MarkUserCollection)]
    (let [marks (vec (. markUserCollection find (new Document "from" username)))]
      (dotimes [i (count marks)]
        (. markUserCollection removeMark (. (. (nth marks i) object) getString "from") (. (. (nth marks i) object) getString "to"))))
    (let [marks (. markUserCollection find (new Document "to" username))]
      (dotimes [i (count marks)]
        (. markUserCollection removeMark (. (. (nth marks i) object) getString "from") (. (. (nth marks i) object) getString "to")))))
  (let [messageCollection (new MessageCollection)]
    (let [messages (vec (. messageCollection findMessageData (new Document "username" username)))]
      (dotimes [i (count messages)] (. messageCollection removeMessage (. (. (. (nth messages i) object) get "_id") toString)))))
  true)

(. ManagerLogic put "control.UserManager$loginUser" loginUser 2)
(. ManagerLogic put "control.UserManager$register" register 2)
(. ManagerLogic put "control.UserManager$removeUser" removeUser 2)
