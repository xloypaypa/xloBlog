(ns
  ^{:author xlo}
  model.db.LockAllCollection
  (:import [config ConfigManager DBConfig]
           [model.lock NameLockImpl]))

(println "test")
(let [db (. (. (. ConfigManager getConfigManager) getConfig DBConfig) getCollections)]
  (doseq [[_ name] db]
    (. (. NameLockImpl getNameLock) lock name)))
