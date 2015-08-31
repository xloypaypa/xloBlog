(ns
  ^{:author xlo}
  control.AcceptUser
  (:import [control UserAccessManager]))

(defn giveAccess [username access]
  (let [user (new UserAccessManager nil)]
    (. user acceptUserRegister admim adminPassword username access)))

(giveAccess "123" 2)

