class Team:
    def __init__(self, id) -> None:
        self.id = id
        self.score = 0


class Match:
    def __init__(self, a, b) -> None:
        self.a = a
        self.b = b
        self.res = -2


class MatchTable:
    def __init__(self) -> None:
        self.table = []


class League:
    def __init__(self, arr) -> None:
        self.arr = []
        for i in arr:
            self.arr.append(Team(i))

    def createTable(self):
        self.teamTable = []
        i = 0
        self.teamTable.append([])
        self.teamTable.append([])
        self.teamTable.append([])
        self.teamTable.append([])
        for t in self.arr:
            self.teamTable[i].append(t)
            if i == 3:
                i = 0
            else:
                i += 1
        self.createMatchTable()

    def createMatchTable(self):
        self.matchTable = []
        self.matchTable.append([])
        self.matchTable.append([])
        self.matchTable.append([])
        self.matchTable.append([])
        for i in range(4):
            for t in range(3):
                for y in range(t+1, 4):
                    self.matchTable[i].append(
                        Match(self.teamTable[i][t], self.teamTable[i][y]))

    def kqVongBang(self, bang, tran, kq):
        # -1,0,1
        # -1 A win
        # 0 draw
        # 1 B win
        if kq == -1:
            self.matchTable[bang][tran].a.score += 2
            self.matchTable[bang][tran].b.score += 0
        if kq == 0:
            self.matchTable[bang][tran].a.score += 1
            self.matchTable[bang][tran].b.score += 1
        if kq == 1:
            self.matchTable[bang][tran].a.score += 0
            self.matchTable[bang][tran].b.score += 2

        for i in range(3):
            for t in range(i+1, 4):
                if self.teamTable[bang][i].score < self.teamTable[bang][t].score:
                    tg = self.teamTable[bang][i]
                    self.teamTable[bang][i] = self.teamTable[bang][t]
                    self.teamTable[bang][t] = tg

    def tuKet(self):
        self.tuKetTable = []

        self.tuKetTable.append(self.teamTable[0][0])
        self.tuKetTable.append(self.teamTable[0][1])

        self.tuKetTable.append(self.teamTable[1][0])
        self.tuKetTable.append(self.teamTable[1][1])

        self.tuKetTable.append(self.teamTable[2][0])
        self.tuKetTable.append(self.teamTable[2][1])

        self.tuKetTable.append(self.teamTable[3][0])
        self.tuKetTable.append(self.teamTable[3][1])

        self.tuKetMatchTable = []
        self.tuKetMatchTable.append(
            Match(self.tuKetTable[0], self.tuKetTable[3]))
        self.tuKetMatchTable.append(
            Match(self.tuKetTable[1], self.tuKetTable[2]))
        self.tuKetMatchTable.append(
            Match(self.tuKetTable[4], self.tuKetTable[7]))
        self.tuKetMatchTable.append(
            Match(self.tuKetTable[5], self.tuKetTable[6]))

    def kqTK(self, tran, kq) -> bool:
        # -1,0,1
        # -1 A win
        # 0 draw
        # 1 B win
        if kq == -1:
            self.tuKetTable.remove(self.tuKetMatchTable[tran].b)
        if kq == 1:
            self.tuKetTable.remove(self.tuKetMatchTable[tran].a)
        if kq == 0:
            if self.tuKetMatchTable[tran].a.score > self.tuKetMatchTable[tran].b.score:
                self.tuKetTable.remove(self.tuKetMatchTable[tran].b)
            elif self.tuKetMatchTable[tran].a.score < self.tuKetMatchTable[tran].b.score:
                self.tuKetTable.remove(self.tuKetMatchTable[tran].a)
            else:
                return True
        return False

    def banKet(self):
        self.halfFinalMatchTable = []
        self.halfFinalMatchTable.append(
            Match(self.tuKetTable[0], self.tuKetTable[3]))
        self.halfFinalMatchTable.append(
            Match(self.tuKetTable[1], self.tuKetTable[2]))


arr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]

l = League(arr)

l.createTable()
for t in l.matchTable:
    print("bang")
    for i in t:
        print("tran: ")
        print(str(i.a.id) + " vs " + str(i.b.id))
    print("-------------------")

l.kqVongBang(0, 4, 1)
l.kqVongBang(0, 1, -1)
l.kqVongBang(0, 2, 0)
l.kqVongBang(0, 3, 0)
for i in l.teamTable[0]:
    print("tran: ")
    print(str(i.id) + " " + str(i.score))
print("-------------------")


l.tuKet()

print("----------------------")

for i in l.tuKetTable:
    print(i.id)

print("----------------------")

for i in l.tuKetMatchTable:
    print(str(i.a.id) + " vs "+str(i.b.id))
print("----------------------")

l.kqTK(0, 1)

l.banKet()

for i in l.halfFinalMatchTable:
    print(str(i.a.id) + " vs "+str(i.b.id))
