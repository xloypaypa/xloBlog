(ns
  ^{:author xlo}
  control.UserManagerLogic
  (:import [model.db UserCollection]
           [control Manager]
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

(defn register-to-manager [name object & paramter]
  (let [method (. (. object getClass) getMethod "invoke" Object Object)]
    ))

(. Manager put "control.UserManager$loginUser" loginUser 2)
(. Manager put "control.UserManager$register" register 2)
