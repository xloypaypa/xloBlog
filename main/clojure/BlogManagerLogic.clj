(ns
  ^{:author xlo}
  control.BlogManagerLogic
  (:import [model.db BlogCollection MarkUserCollection MessageCollection]
           [org.bson Document BsonArray BsonDocument BsonDateTime BsonString]
           [config LengthLimitConfig]
           [java.util Date]
           [control ManagerLogic]))

(defn addDocument [username password title body type]
  (if (or (nil? title) (nil? body) (nil? type)) false
    (let [lengthLimitConfig (. LengthLimitConfig getConfig)]
      (if
        (or (> (count title) (. lengthLimitConfig getLimit "documentTitle"))
          (> (count body) (. lengthLimitConfig getLimit "documentBody"))) false
        (do (let [blogCollection (new BlogCollection)]
              (. blogCollection addDocument username title body (new Date) type))
          (let [markUserCollection (new MarkUserCollection)
                messageCollection (new MessageCollection)
                marks (vec (. markUserCollection find (new Document "to" username)))]
            (dotimes [i (count marks)]
              (. messageCollection addMessage (. (. (nth marks i) object) getString "to") username title (new Date))))
          true)))))

(defn addReply [username password documentID reply]
  (if (or (nil? documentID) (nil? reply)) false
    (let [lengthLimitConfig (. LengthLimitConfig getConfig)]
      (if (> (count reply) (. lengthLimitConfig getLimit "documentBody")) false
        (let [blogCollection (new BlogCollection)
              document (. blogCollection getDocument documentID)]
          (if (nil? document) false
            (let [replyList (new BsonArray)
                  pastList (vec (. (. document object) get "reply"))
                  nowReply (new BsonDocument)]
              (. nowReply put "author" (new BsonString username))
              (. nowReply put "data" (new BsonDateTime (. (new Date) getTime)))
              (. nowReply put "reply" (new BsonString reply))
              (dotimes [i (count pastList)] (. replyList add (nth pastList i)))
              (. replyList add nowReply)
              (. (. document object) put "reply" replyList)
              (let [messageCollection (new MessageCollection)]
                (. messageCollection addMessage
                  (. (. document object) getString "author") username (str "reply: " reply) (new Date)))
              true)))))))

(. ManagerLogic put "control.BlogManager$addDocument" addDocument 5)
(. ManagerLogic put "control.BlogManager$addReply" addReply 4)