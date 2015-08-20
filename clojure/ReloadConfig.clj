(ns
  ^{:author xlo}
  config.ReloadConfig
  (:import [config ConfigManager]))

(. (. ConfigManager getConfigManager) reload)