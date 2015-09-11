(ns
  ^{:author xlo}
  control.MessageManagerLogic
  (:import [model.db MessageCollection UserCollection]
           [java.util Date LinkedList]
           [config LengthLimitConfig]
           [control ManagerLogic]
           [org.bson Document]))

(defn addMessage [username password message aimUser]
  (if (or (nil? message) (nil? aimUser)) false
    (let [lengthLimitConfig (. LengthLimitConfig getConfig)]
      (if (> (count message) (. lengthLimitConfig getLimit "message")) false
        (let [aimUserData (. (new UserCollection) getUserData aimUser)]
          (if (nil? aimUserData) false
            (do (. (new MessageCollection) addMessage aimUser username message (new Date)) true)))))))

(defn getMessage [username passwrod id manager event]
  (if (nil? id) false
    (let [data (. (new MessageCollection) getMessageData id)]
      (if (or (nil? data) (not= username (. (. data object) getString "username"))) false
        (do (. manager addSuccessMessage event (. (. data object) toJson)) true)))))

(defn readMessage[username password id]
  (if (nil? id) false
    (let [data (. (new MessageCollection) getMessage id)]
      (if (or (nil? data) (not= username (. (. data object) getString "username"))) false
        (do (. (. data object) put "read" true) true)))))

(defn getAllMessage[username password aimUser manager event]
  (let [aimList (vec (. (new MessageCollection) findMessageData (new Document "username" aimUser)))
        ans (new LinkedList)]
    (dotimes [i (count aimList)]
      (let [now (nth aimList i)
            object (. now object)]
        (. ans add object)))
    (. manager addSuccessMessage event ans) true))

(. ManagerLogic put "control.MessageManager$addMessage" addMessage 4)
(. ManagerLogic put "control.MessageManager$getMessage" getMessage 5)
(. ManagerLogic put "control.MessageManager$readMessage" readMessage 3)
(. ManagerLogic put "control.MessageManager$getAllMessage" getAllMessage 5)
(. ManagerLogic put "control.MessageManager$getUserAllMessage" getAllMessage 5)