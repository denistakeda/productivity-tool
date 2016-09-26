(ns productivity-tool.app
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [devtools.core :as devtools]
            [productivity-tool.handlers]
            [productivity-tool.subs]

            [productivity-tool.views :as views]
            [productivity-tool.config :as config]
            [productivity-tool.utils.firebase :as f]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")
    (devtools/install!)))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "container")))

;; Subscribe to the database events
(defn subscribe-to-user
  []
  (f/listen-to-user
    #(re-frame/dispatch [:fetch-user-info {:name (.-displayName %)
                                           :email (.-email %)
                                           :photoUrl (.-photoURL %)
                                           :uid (.-uid %)}])))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root)
  (subscribe-to-user))
