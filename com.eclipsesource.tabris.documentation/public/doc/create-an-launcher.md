### **Creating a launcher is pretty similar to [connecting it](https://eclipsesource.com/products/tabris/eclipse-rap-documentation/tabris-for-eclipse-rap-running-a-mobile-client/). The following steps describe how to create your own Tabris client.**

1. Access to [Tabris.js registry](https://npm.tabrisjs.com/) requires a license. Request it [here.](https://eclipsesource.com/products/tabris/pricing/)
2. Fork the simple template application from the https://github.com/eclipsesource/tabris-remote-app. This is a simple tabris.js application (launcher) with minimal UI for entering the remote server URL (entry point). The app is written in TypeScript. More information about tabris.js can be found at:
    [https://tabrisjs.com/documentation/latest/getting-started.html](https://tabrisjs.com/documentation/latest/getting-started.html)
    [https://tabrisjs.com/documentation/latest](https://tabrisjs.com/documentation/latest)
3. Customize this application to fit your needs. You can remove the UI completely and start the remote entry point directly.
4. Use npm "adduser" command ([https://docs.npmjs.com/cli/adduser](https://docs.npmjs.com/cli/adduser)) in the root of the project to store the credentials for https://npm.tabrisjs.com/ npm registry in .npmrc file. The file is located in your home directory. This file contains your private NPM token.
5. Customize the cordova/config.xml to add your application icons, hooks or plugins as described in [https://tabrisjs.com/documentation/latest/build.html](https://tabrisjs.com/documentation/latest/build.html).
6. Build your app with the Tabris.js build service by signing in to [https://tabrisjs.com](https://tabrisjs.com)Â or locally with Tabris CLI as described in https://tabrisjs.com/documentation/latest/build.html .
7. If you are using the online Tabris.js build service, set your NPM token in the project settings environment variable called NPM\_TOKEN
