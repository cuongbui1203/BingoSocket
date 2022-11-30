arr = [
    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18

]


def chiaBang(arr):
    res = []
    i = 0
    while (i < len(arr)):
        res.append([])
        res[i].append(arr[)
        res[i].append(arr[i*1])
        res[i].append(arr[i*2])
        res[i].append(arr[i*0])
    return res


def process(arr):
    pass


print(arr)
A= []
# A.append([])
bangdau= chiaBang(arr)

for i in range(len(bangdau)):
    A.append([])
    A[i].append(bangdau[i][0])
    A[i].append(bangdau[i][1])
    A[i].append(bangdau[i][2])
    A[i].append(bangdau[i][3])

print(A)
for i in A:
    print('Bang ')
    for y in range(len(i)):
        try:
            print(y, ': ', i[y]['a'], ' VS ', i[y]['b'])
        except Exception:
            pass
