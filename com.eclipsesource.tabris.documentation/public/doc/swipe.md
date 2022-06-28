## Swipe

One of the most commonly used gestures on mobile devices is swiping. Swiping between different screens (Pages) is the default navigation on the home screens of all major mobile operating systems. Tabris introduces swiping with a simple to implement `Swipe` widget that can load items on demand.

![](images/swipe.gif)

## Creating a Swipe Widget

A `Swipe` widget behaves like any other widget. It has bounds and content. For this reason, it needs to be created with a parent element.

```
Swipe result = new Swipe( parent, new MySwipeItemProvider() );
```

One special thing here is that the `Swipe` widget does not need style flags. Instead, you need to pass in a `SwipeItemProvider`. This provider will produce `SwipeItem` instances.

## The SwipeItem

The `SwipeItem` is a container for the controls you will use within your `Swipe` widget. A `Swipe` widget can hold multiple `SwipeItems.` It's comparable to a Menu or Table with the exception that only one `SwipeItem` can be visible at the same time. A SwipeItem defines callback methods like `activate, deactivate and load`. These methods will be called when a user swipes through the items.

- **load:** Will be called when the item needs to create its controls.
- **activate:** Will be called when the item becomes active. Basically, this means when a user swipes to this item.
- **deactivate:** Will be called when a user leaves an item. Basically, this means when a user swipes away (left or right) from this item.

## Preloading

A `SwipeItem` can be preloadable. This means that its content will be created by calling the load method before it's visible. This will speed up the transition between items. To explain this we will use a simple example. Let's say your `Swipe` widget consist of 5 `SwipeItems`. We can visualize this using a simple array here `[0, 1, 2, 3, 4]`. The UX would benefit when you swipe from item 1 to item 2 and the content of item 2 will be visible already during the transition. To realize this the `Swipe` widget needs to call the load method of item 2 when item 1 becomes visible. Thus item 2 has to be preloadable. It's preloadable when the item implements its Preloadable with true.

To define how many items will be preloaded you can define a cache size on the Swipe widget. By defining a cache size of 1 the widget will preload the next item in both directions (left, right) when the item in between is visible. In our example, this means when item 1 is visible the widget will preload item 0 and 2.

## The Provider

The `Swipe` widget needs a `SwipeItem` provider that is responsible for creating the `SwipeItems`. The provider is an implementation of the interface `SwipeItemProvider`. It needs to implement two methods. These are:

- **getItem:** This method will be called with an index that should return the SwipeItem for the specified index.
- **getItemCount:** This tells the Swipe widget how many items exist.

_Please note: The navigation between_ `SwipeItems` _can also be done programatically using the_ `show` _method on the_ `Swipe` _widget._

## Demo

A simple demo on how to use the `Swipe` widget can be found on [github](https://github.com/eclipsesource/tabris-demos/blob/master/com.eclipsesource.tabris.demos/src/com/eclipsesource/tabris/demos/entrypoints/SwipeDemo.java).
