(ns re-share.core
  "Common re-ops functions"
  (:require
   [taoensso.timbre :refer  (refer-timbre)]
   [minderbinder.time :refer  (parse-time-unit)])
  (:import
   java.io.StringWriter
   java.io.PrintWriter
   java.security.MessageDigest
   java.math.BigInteger))

(refer-timbre)

(defn find-port
  "find the first available port within a given range"
  [from to]
  (first
   (filter
    (fn [p] (try (with-open [s (java.net.ServerSocket. p)] true) (catch Exception e false))) (range from to))))

(defn md5 [^String s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        raw (.digest algorithm (.getBytes s))]
    (format "%032x" (BigInteger. 1 raw))))

(defn error-m [e]
  (let [sw (StringWriter.) p (PrintWriter. sw)]
    (.printStackTrace e p)
    (error (.getMessage e) (.toString sw))))

(defn measure [f]
  (let [starttime (System/nanoTime)
        r (f)
        endtime (System/nanoTime)]
    {:start starttime :end endtime :time (/ (- endtime starttime) 1e9) :result r}))

(defn gen-uuid []
  (.replace (str (java.util.UUID/randomUUID)) "-" ""))
