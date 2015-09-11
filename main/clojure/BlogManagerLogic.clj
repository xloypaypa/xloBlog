(ns
  ^{:author xlo}
  control.BlogManagerLogic
  (:import [model.db BlogCollection MarkUserCollection MessageCollection]
           [org.bson Document BsonArray BsonDocument BsonDateTime BsonString]
           [config LengthLimitConfig ConfigManager ReturnCodeConfig]
           [java.util Date LinkedList]
           [control ManagerLogic BlogManager]))

(defn sendDocumentList [manager event message]
  (let [aimList (vec (. (new BlogCollection) findDocumentListData message))
        ans (new LinkedList)]
    (dotimes [i (count aimList)]
      (let [now (nth aimList i)
            object (. now object)
            body (. object get "body")
            preview (if (> (count body) 100) (subs body 0 100) body)
            nowMap {"id" (str (. object get "_id")),
                    "title" (. object get "title"),
                    "author" (. object get "author"),
                    "time" (. object get "time"),
                    "reader" (. object get "reader")
                    "preview" preview}]
        (. ans add nowMap)))
    (. manager addSuccessMessage event ans)))

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

(defn getDocument [id manager event returnCodeConfig]
  (let [object {"return" (. returnCodeConfig getCode "not found")}]
    (. manager addFailMessage event object))
  (if (nil? id) false
    (let [data (. (new BlogCollection) getDocumentData id)]
      (if (nil? data) false
        (let [object (. (. data object) toJson)]
          (. manager addSuccessMessage event object) true)))))

(defn addReader [id]
  (let [data (. (new BlogCollection) getDocument id)]
    (if (nil? data) false
      (let [object (. data object)
            val (+ (. object getInteger "reader" 0) 1)]
        (. object put "reader" (int val)) true))))

(defn getAuthorTypeDocumentList [author typeMessage manager event returnCodeConfig]
  (let [object {"return" (. returnCodeConfig getCode "not found")}]
    (. manager addFailMessage event object))
  (if (or (nil? author) (nil? typeMessage)) false
    (let [document (new Document)]
      (sendDocumentList manager event (. (. document append "author" author) append "type" typeMessage))
      true)))

(defn getTypeDocumentList [typeMessage manager event returnCodeConfig]
  (let [object {"return" (. returnCodeConfig getCode "not found")}]
    (. manager addFailMessage event object))
  (if (nil? typeMessage) false
    (let [document (new Document)]
      (sendDocumentList manager event (. document append "type" typeMessage))
      true)))

(defn getAuthorDocumentList [author manager event returnCodeConfig]
  (let [object {"return" (. returnCodeConfig getCode "not found")}]
    (. manager addFailMessage event object))
  (if (nil? author) false
    (let [document (new Document)]
      (sendDocumentList manager event (. document append "author" author))
      true)))

(. ManagerLogic put "control.BlogManager$addDocument" addDocument 5)
(. ManagerLogic put "control.BlogManager$addReply" addReply 4)
(. ManagerLogic put "control.BlogManager$getDocument" getDocument 4)
(. ManagerLogic put "control.BlogManager$addReader" addReader 1)
(. ManagerLogic put "control.BlogManager$getAuthorTypeDocumentList" getAuthorTypeDocumentList 5)
(. ManagerLogic put "control.BlogManager$getTypeDocumentList" getTypeDocumentList 4)
(. ManagerLogic put "control.BlogManager$getAuthorDocumentList" getAuthorDocumentList 4)