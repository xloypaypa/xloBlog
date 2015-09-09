(ns
  ^{:author xlo}
  net.post
  (:import [net.post PostInfoSolver]
           [config PostConfig]
           [net.sf.json JSONObject]))

(defn fun[]
  (for [i (range 3)]
    (println i))
  )

(deftype PostInfoSolverImpl [] PostInfoSolver
  (solveInfo[this postInfo message requestSolver]
;    (let [manager (. postInfo getManager)
;          access (. postInfo getAccess)
;          method (. postInfo getMethod)
;          methodDataType (. postInfo getMethodDataType)
;          methodData (. postInfo getMethodData)]
;      )
    (fun)
    ))

(def config (. PostConfig getConfig))
(def infoList (. config getPostInfo))
(def info (. infoList get 1))
(def solver (new PostInfoSolverImpl))
(. solver solveInfo info "{}" nil)