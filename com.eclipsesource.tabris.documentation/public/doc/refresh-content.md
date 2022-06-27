## Refresh Content

In many applications, it's required to refresh some content. The mechanics of this feature should be simple. One pattern that has already become a standard for mobile users is "Pull to Refresh" which means pull the user interface down to force the app to refresh its current view.

![](images/pull-to-refresh.gif)

## Trees and Tables

The most common place to add pull to refresh behavior is a Tree or a Table. You can simply do this with this snippet:

```
Tree tree = new Tree( shell, SWT.BORDER );
final RefreshHandler handler = new RefreshHandler();
handler.setMessage( "Fetching new Data..." );
handler.addRefreshListener( new RefreshListener() {
  @Override
  public void refresh() {
    // fetch new data and update the tree here...
    ...
    handler.done();
  }
} );
Widgets.onTree( tree ).setRefreshHandler( handler );
```

Please note that refreshing is an asynchronous operation. You need to call the `done` method of the handler when you have finished the job.

## RefreshComposite

Sometimes a refresh is needed when no Tree or Table is involved. For this reason the RefreshComposite exists which works almost the same as the RefreshHandler. Using it can look like this:

```
final RefreshComposite composite = new RefreshComposite( shell, SWT.NONE );
composite.setMessage( "Fetching new Data..." );
composite.addRefreshListener( new RefreshListener() {
  @Override
  public void refresh() {
    // fetch new data and update the UI here...
    ...
    composite.done();
  }
} );
```
