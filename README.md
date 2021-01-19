# DragDemo
### 一个随意拖拽的自定义View

可能速度有点快，大概就是手指拖动移动图片的位置。

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

