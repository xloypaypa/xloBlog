(ns
  ^{:author xlo}
  control.AcceptUser
  (:import [control UserManager UserManager])
  (:import [control UserAccessManager]))

(defn giveAccess [username access]
  (let [user (new UserAccessManager nil)]
    (. user acceptUserRegister admim adminPassword username accessType accessValue)))

(giveAccess "123" 2)

