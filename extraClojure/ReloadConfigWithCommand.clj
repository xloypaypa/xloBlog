(ns
  ^{:author xlo}
  config.ReloadConfigWithCommand
  (:import [config ConfigManager ShowFileTypeConfig]))

(defn reloadAim [config]
  (. (. ConfigManager getConfigManager) reloadConfig config))

(reloadAim ShowFileTypeConfig)