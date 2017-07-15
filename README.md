SimpleRecyclerView
=====

Simplest RecyclerView extention for implementing pagination (on reach list bottom listener, displaying progress bar) and empty view (nothing found) with RecyclerView

Download
--------

```gradle
dependencies {
   compile 'com.myhexaville:simple-recyclerview:0.1.1'
}
```

Setup
-----
Add SimpleRecycerView in XML

```xml
<com.myhexaville.simplerecyclerview.SimpleRecyclerView
            android:layout_width="match_parent"
            android:id="@+id/list"
            android:layout_height="match_parent"/>
```
Implement SimpleRecyclerView.Adapter, RecyclerView.ViewHolder implementation is same as with default RecyclerView

```java
public class Adapter extends SimpleRecyclerView.Adapter<Holder> {
    public List<String> list;

    public Adapter(List<String> list) {
        this.list = list;
    }

    @Override
    public Holder onCreateHolder(ViewGroup parent) {
       // create View Holder just like you did before
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void onBindHolder(Holder holder, int position) {
        // bind view holder just like you did before
    }
}
```

Setup in Java

```java
simpleRecyclerView.setLayoutManager(new LinearLayoutManager(context));
simpleRecyclerView.setAdapter(new Adapter(list));
```

Features
--------

### Pagination
By default pagination and footer ProgressBar is disabled, to enable it simply add those lines
```java
simpleRecyclerView.setOnLoadMoreListener(() -> {
  // fetch data from your server and add to your list
  
  // this will do notifyItemRangeInserted
  // no need to notify recyclerView adapter yourself
  simpleRecyclerView.setDoneFetching();
});
```
This listener will be called each time you reach bottom of the list and will display default ProgressBar footer
#### Custom ProgressBar footer
If you want to use your own ProgressBar footer - create your own progress_footer.xml
```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <ProgressBar
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center" />
</FrameLayout>
```
and pass to SimpleRecyclerView

```xml
<com.myhexaville.simplerecyclerview.SimpleRecyclerView
            android:layout_width="match_parent"
            android:id="@+id/list"
            app:progress_layout="@layout/progress_footer"
            android:layout_height="match_parent"/>
```
#### No More To Fetch
If you server returns no items and you don't want onFetchMore listener to be called anymore just like don't display ProgressBar any more - call this method
```java
simpleRecyclerView.setOnLoadMoreListener(() -> {
  ...
  if (noStuffFetchedFromServer) {
     simpleRecyclerView.setNoMoreToFetch();
  }
});
```
### Empty View
By default empty view (Nothing found, when list is empty) is disabled. To display it - create your empty_layout.xml
```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="Nothing Found" />
</FrameLayout>
```
And pass to SimpleRecyclerView
```xml
<com.myhexaville.simplerecyclerview.SimpleRecyclerView
            android:layout_width="match_parent"
            android:id="@+id/list"
            app:empty_layout="@layout/empty_layout"
            android:layout_height="match_parent"/>
```

Sample
------
There's a sample of using this library in this repo
