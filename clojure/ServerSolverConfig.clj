(ns
  ^{:author xlo}
  net.ServerSolverConfig
  (:import [net CommandServerSolver AimSolverChooser]))

(let [chooser (. CommandServerSolver chooserMap)]
  (. chooser put "test"
    (reify AimSolverChooser
      (isThisSolver [this requestSolver] false)
      (getSolver [this requestSolver]))
    )
  (println (. (. chooser get "test") isThisSolver nil)))
