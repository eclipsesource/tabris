## Scrolling

Often you will find yourself in the situation where you need to scroll some content. This is basically because mobile devices have a very small screen. In Tabris you have two ways to realize scrolling.

## ScrollingComposite

If you know SWT you probably know the `ScrolledComposite` (please note the different names). The `ScrolledComposite` works in Tabris out-of-the-box. Anyway, the `ScrolledComposite` has a hard to use API that is not very self-explanatory. For this reason, we have created the `ScrollingComposite`. It can be used as any other `Composite`. So, if you want to have a `Composite` that just grows with its content, you can just replace your new `Composite( parent, style )` with this snippet:

```
Composite composite = new ScrollingComposite( parent, SWT.VERTICAL | SWT.HORIZONTAL );
```

The `ScrollingComposite` can be used with `SWT.VERTICAL/SWT.V_SCROLL and SWT.HORIZONTAL/SWT.H_SCROLL` to enable vertical or horizontal scrolling. If you need both, just combine them with the | operator.

## ScrolledComposite

We recommend you to go with `ScrollingComposite`, but the SWT `ScrolledComposite` works in Tabris as expected. So, feel free to choose the one you like better.
