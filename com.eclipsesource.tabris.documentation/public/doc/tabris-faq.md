### Under which license is Tabris available?

Everything that runs on the server side is licensed open source under the [EPL v1.0](https://eclipse.org/org/documents/epl-v10.php). The Tabris clients are licensed per seat. This means we use a developer licensing model. You need to purchase a license for each client platform (Android/iOS). We also offer an enterprise source code license for a flat fee. [Read more about pricing here.](https://eclipsesource.com/en/products/tabris/pricing/)

### Where can I find example code?

We host a set of examples on our public [EclipseSource GitHub repository.](https://github.com/eclipsesource/tabris-demos)

### What is RAP?

The Eclipse [RAP project](https://eclipse.org/rap/) (Remote Application Platform) provides a powerful widget toolkit and integrates well with proven technologies such as OSGi and JEE. You can write your application entirely in Java, re-use code and benefit from first-class IDE tools.

### How are RAP and Tabris related?

RAP (Remote Application Platform) provides the server part of Tabris. It communicates using a JSON based protocol to support clients for all kind of platforms and programming languages.

### How is Tabris developed?

The RAP server and the clients are developed using [TDD](https://en.wikipedia.org/wiki/Test-driven_development). The Server and Android client are written in Java, the iOS client in Objective-C.

### Who should I contact with questions?

For technical questions head over to [StackOverflow](https://stackoverflow.com/search?q=tabris), for all other questions, [use this form](https://eclipsesource.com/about/contact-us/).

### Is there training available?

You need to know RAP and SWT and have knowledge about how to design a mobile UI. Head over to the [EclipseSource training](https://eclipsesource.com/services/training/) site to get an overview.

### Will my app work in the Apple App Store or Google's Play Store?

Yes, as long you comply with the App Store rules. The decision is up to the Store provider.

### Will my App work offline?

Apps built with Tabris require a connection to a server. This is by design. The RAP server has all the business logic and a model of the client UI. It's similar to a Thin-Client architecture where only the currently displayed data is sent to the client. However, with the latest Tabris for Eclipse RAP 3.6 release, part of the UI can be created in JavaScript (Tabris.js) without a connection to the server.

### Will my RCP/RAP application run with Tabris?

It depends. If you plan to connect to an existing application please be aware that the Eclipse workbench is not a good match for a mobile device. Often it will be better to reuse parts of your UI with a bit of refactoring and by limiting yourself to SWT, JFace and OSGi Services (e.g. EventAdmin). And - of course - make sure to use the Tabris Target.

If you encounter problems migrating your existing application to Tabris, [contact us](https://eclipsesource.com/about/contact-us/) or use our forum at [StackOverflow](https://stackoverflow.com/search?q=tabris).
