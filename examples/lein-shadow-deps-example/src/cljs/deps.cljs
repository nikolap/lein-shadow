{:npm-deps              {"create-react-class" "15.6.3"
                         "react"              "16.8.6"
                         "react-dom"          "16.8.6"}
 :resolutions           {"dependency-of-some-dep" "0.1.2"}
 :scripts               {"preinstall" "npx npm-force-resolutions"}

 ;; this is needed to get the `resolutions` and `scripts` keys in the package.json
 :replace-project-json? true}
