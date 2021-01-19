# DragDemo
### 一个随意拖拽的自定义View

#### 可能速度有点快，大概就是图片随着手指的移动进行拖拽。
#### 完成度比较低，主要是一些自定义控件知识，有需求的同学可以下载下来进行改造。

![image]( https://github.com/limbowangqi/DragDemo/blob/master/dragDemo.gif)

## 使用方法
# 1.添加依赖
```
implementation 'com.github.limbowangqi:DragDemo:0.0.1'
```

# 2.使用
```
val drag_imag_view = findViewById<DragImageView>(R.id.drag_image_view)

drag_imag_view.setData(ArrayList<Int>().apply {
    add(R.mipmap.img1)
    add(R.mipmap.img2)
    add(R.mipmap.img3)
    add(R.mipmap.img4)
    add(R.mipmap.img5)
})
```

