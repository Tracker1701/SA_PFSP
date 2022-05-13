import matplotlib.pyplot as plt

mylist = []     # 用来保存目标函数变化数据
inputfile = open("trace.txt", 'r', encoding='utf-8')
while 1:
    templine = inputfile.readline()
    if templine == '':
        break
    templine = templine.strip('\t\n')
    templist = templine.split('\t')
    for iterm in templist:      # 导入数据，并将字符串转换为数字存储
        mylist.append(int(iterm))
inputfile.close() #以上部分输入目标函数的变化

# 作折线图
plt.plot(mylist, linewidth=1)
plt.title("目标函数变化图", fontsize=20)
plt.xlabel("x", fontsize=10)
plt.ylabel("f", fontsize=14)
plt.tick_params(axis='both',labelsize=10)
plt.savefig("./trace.jpg")  # 保存图片为trace.jpg
plt.show()