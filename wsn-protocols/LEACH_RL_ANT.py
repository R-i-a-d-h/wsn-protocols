import math 
import random
import numpy as np
import matplotlib.pyplot as plt
from decimal import *
# Field Dimensions in meters %
xm=100
ym=100
x=0 # added for better display results of the plot
y=0 # added for better display results of the plot
# Number of Nodes in the field %
n=100
# Number of Dead Nodes in the beggining %
dead_nodes=0
# Coordinates of the Sink (location is predetermined in this simulation) %
sinkx=50
sinky=50
r=40
# Energy Values %%%
#Initial Energy of a Node (in Joules) % 
Eo=0.2 #units in Joules
#Energy required to run circuity (both for transmitter and receiver) %
Eelec=50*10**(-9)# units in Joules/bit
ETx=50*10**(-9) # units in Joules/bit
ERx=50*10**(-9) # units in Joules/bit
#Transmit Amplifier Types %
Eamp=100*10**(-12) # units in Joules/bit/m^2 (amount of energy spent by the amplifier to transmit the bits)
# Data Aggregation Energy %
EDA=5*10**(-9)# units in Joules/bit
# Size of data package %
k=4000 # units in bits
# Suggested percentage of cluster head %
p=0.1# a 5 percent of the total amount of nodes used in the network is proposed to give good results
#Number of Clusters %
No=p*n
# Round of Operation %
rnd=0
# Current Number of operating Nodes %
operating_nodes=n
transmissions=0
temp_val=0
flag1stdead=0
print(r)
print(Eelec)
print(Eamp)
print(EDA)
print(ETx)
print(ERx)
###############################################################################################################################
import random
class Ant :
    id=None  # sensor's ID number
    trail=None   # X-axis coordinates of sensor node
    lastCity=None   # Y-axis coordinates of sensor node
    closeTrail=None   # nodes energy levels (initially set to be equal to "Eo"
    def __init__(self, id, lastCity):
        self.id = id
        self.trail = []
        self.lastCity = lastCity
        self.closeTrail = 0
    def visited(self, i):
        for j  in range(len(self.trail)) :
            city = self.trail[j]
            if (city == i) :
                return True
        return False
    def visitCity(self,city) :
        if (city==self.lastCity):
            self.closeTrail = 1
        self.trail.append(city)  #add to trail
        
    def checkCity(self,city ,graph) :
        currentCity = self.trail[len(self.trail)-1]
        if graph[currentCity][city] == 0 :
            return False 
        return True
    def trailHelthy(self):
        currentCity = self.trail[len(self.trail)-1]
        if currentCity == self.lastCity :
            return True
        return False
    def trailLength(self,graph) :
        length = 0;
        for  i  in range(len(self.trail)-1) :
            length += graph[self.trail[i]][self.trail[i+1]]
        return length
    def clear(self) :
        self.trail = []
        self.closeTrail=0


class AntColonyOptimization:
    s=""
    c=None  # number of trails
    alpha=None   # X-axis coordinates of sensor node
    beta=None   # Y-axis coordinates of sensor node
    evaporation=None   # nodes energy levels (initially set to be equal to "Eo"
    q=None
    antFactor=None 
    maxIterations = None
    numberOfCities = None
    numberOfAnts = None
    graph =  None  
    randomFactor = None
    trails = None
    probabilities = None
    ants = None
    bestTourOrder =None
    bestTourLength =None
    startIndex =None
    lastIndex =None
    
    def __init__(self,tr,  al,  be, ev,
                    q, af, rf, ite, noOfCities,graph ,st ,la):
        self.c=tr
        self.alpha=al 
        self.beta=be
        self.evaporation=ev 
        self.q=q 
        self.antFactor=af 
        self.randomFactor=rf
        self.maxIterations=ite
        self.startIndex =st
        self.lastIndex=la
        self.graph =graph
        self.numberOfCities = noOfCities
        self.numberOfAnts = int(self.numberOfCities * self.antFactor)
        self.trails = [[0 for _ in range(self.numberOfCities)] for _ in range(self.numberOfCities)]
        self.probabilities = [0 for i in range(self.numberOfCities)]
        self.ants= []
        self.bestTourOrder =[]
        self.bestTourLength =0
        
        for i in range(self.numberOfAnts) :
            ant = Ant(i,self.lastIndex)
            self.ants.append(ant)
        self.s+='INIT :'
        self.s+='\nGraph :'+str(self.graph)
        self.s+='\nNumber Of Cities :'+str(self.numberOfCities)
        self.s+='\nTrails :'+str(self.trails)
        self.s+='\nAnts : '
        for i in range(self.numberOfAnts) :
            ant = self.ants[i]
            self.s+='\nAnt : '+str(ant.id) +' Trail : '+str(ant.trail)
        self.s+='\nNumber Of Ants :'+str(self.numberOfAnts)
        self.s+='\nProbabilities :'+str(self.probabilities)
        self.s+='\nEvaporation :'+str(self.evaporation)
        self.s+='\nStart Index :'+str(self.startIndex)
        self.s+='\nLast Index : '+str(self.lastIndex)
        self.s+='\nQ :'+str(self.q) 
        self.s+='\nAlpha :'+str(self.alpha)
        self.s+='\nBeta : '+str(self.beta)
        self.s+='\nNumber Of Iterations : '+str(self.maxIterations)

    def startAntOptimization(self) :
        for i in range(1) :
            self.s+='\nAttempt : '+str(i)
            self.solve()
            self.s+='\n'
    
        #print( self.s)
    def solve(self) :
        self.setupAnts()
        self.clearTrails()
        
        for i in range(self.maxIterations) :
            self.s+= '\n-----------------------------------------------------------------------------------'
            self.s+= '\n-----------------------------------------------------------------------------------'
            self.moveAnts()
            self.updateTrails()
            self.updateBest()
            for i in range(len(self.ants)) :
                ant = self.ants[i]
                ant.clear();
                ant.visitCity(self.startIndex)   
        return " addc"
        
        
    def antTest(self):
        for i in range(len(self.ants)) :
            ant  = self.ants[i]
            if  ant.closeTrail == 0:
                return False 
        return True
                
    def moveAnts(self) :
        while(not self.antTest()) :
            for i in range(len(self.ants)) :
                ant = self.ants[i]
                self.s+='\nAnt : '+str(ant.id)+' Trail : '+str(ant.trail)
                if ant.closeTrail==0:
                    #print("\nAnt : "+str(ant.id)+' Trail'+str(ant.trail))
                    city = self.selectNextCity(ant)
                    if(city== -1) :
                        ant.closeTrail = 1
                    else : 
                         ant.visitCity(city)
                   #print("\nAnt : "+str(ant.id)+' Trail'+str(ant.trail))
                    self.s+='\nAnt : '+str(ant.id)+' Trail : '+str(ant.trail)
                    self.s+='\n.................................................................'
         
    
    def updateTrails(self) :
        for i in range(self.numberOfCities):
            for j in range(self.numberOfCities) :
                self.trails[i][j] *= self.evaporation
        for j in range(len(self.ants)) :
            ant  = self.ants[j]
            if ant.trailHelthy() :
                contribution = self.q / ant.trailLength(self.graph)
                for i in range(len(ant.trail)-1) :
                    self.trails[ant.trail[i]][ant.trail[i+1]] += contribution
    
    
    def updateBest(self) :
        if len(self.bestTourOrder)==0 :
            for j in range(len(self.ants)) :
                ant =  self.ants[j]
                if ant.trailHelthy() : 
                    self.bestTourOrder = self.ants[j].trail 
                    self.bestTourLength = self.ants[j].trailLength(self.graph)
    
        for j in range(len(self.ants)) :
            ant  = self.ants[j]
            if ant.trailLength(graph) < self.bestTourLength and ant.trailHelthy():
                self.bestTourLength = ant.trailLength(self.graph)
                self.bestTourOrder = []
                self.bestTourOrder = ant.trail

        self.s+="\nBest Trail : "+str(self.bestTourOrder)
        self.s+="\nBest Length : "+str(self.bestTourLength)
        self.s+="\nTrails : "+str(self.trails)
                
            
    def selectNextCity(self,ant):
        
        t = random.randint(0,self.numberOfCities+1)
        if random.random() <  self.randomFactor:
            cityIndex=-999
            for i in range(self.numberOfCities) :
                if(i==t and ant.checkCity(i ,self.graph)and not ant.visited(i)) :
                    cityIndex=i
                    break;
            if(cityIndex!=-999) :
                self.s+=" use : The random way !"
                return cityIndex
           
        currentCity = ant.trail[len(ant.trail)-1]
        pheromone = 0.0
        for i in range(self.numberOfCities):
            if self.graph[currentCity][i] != 0 and not ant.visited(i):
                pheromone += (self.trails[currentCity][i]**self.alpha) * (1.0 / self.graph[currentCity][i]**self.beta)
        for j in range(self.numberOfCities):
            if (self.graph[currentCity][j] == 0) or ant.visited(j)   :
                self.probabilities[j] = 0.0
            else : 
                numerator = (self.trails[currentCity][j]**self.alpha) * (1.0 / self.graph[currentCity][j]**self.beta)
                self.probabilities[j] = numerator / pheromone
        r = random.random()
        #print(str(r)+" probabilities " +str(self.probabilities))
        total = 0
        for i in range(self.numberOfCities):
            total += self.probabilities[i]
            if(total>=r) :
                return int(i) 
        
        return -1

    def setupAnts(self) :
        self.s+="\nSetupAnts : "
        for i in range(len(self.ants)) :
            ant = self.ants[i]
            ant.clear()
            ant.visitCity(self.startIndex)
            self.s+="\nAnt : "+str(ant.id)+' Trail'+str(ant.trail)
            #print("\nAnt : "+str(ant.id)+' Trail'+str(ant.trail))
    def clearTrails(self) : 
        for i in range(self.numberOfCities):
            for j in range(self.numberOfCities) :
                self.trails[i][j]= self.c
#######################################################################################################################################################
class City :
    id=None  
    x=None   
    y=None   
    e=None  
    cType= None
    eid = None
    def __init__(self,id, x,y,e,eid):
        self.id = id
        self.e = e
        self.x = x
        self.y = y
        self.cType = 0
        self.eid =eid 
class Node:
    id=None  # sensor's ID number
    x=None   # X-axis coordinates of sensor node
    y=None   # Y-axis coordinates of sensor node
    e=None   # nodes energy levels (initially set to be equal to "Eo"
    q=None
    rid=None 
    dist = None
    dch = None
    role = None
    cond = None
    chid =None
    dts = None
    rwd = None
    def __init__(self, id, x,y ,e,dist,dts =-1,q = 0,rid =-1,role=0,cond = 1,chid =-1,rwd= 0):
        self.id = id
        self.e = e
        self.x = x
        self.y = y
        self.q = q
        self.rID = rid
        self.dist = dist
        self.dts = dts
        self.role = role
        self.cond = cond
        self.chid = chid
        self.rwd = rwd
    def toString(self) :
        s=""
        s+='\nID :'+str(self.id)
        s+='\nRole :'+str(self.role)
        s+='\nchID :'+str(self.chid)
        s+='\nE :'+str(self.e)
        #self.s+='\nID :'+str(self.id)
        #self.s+='\nID :'+str(self.id)
        return s
        
class Sets:
    nodeSets=[]       
class graphPlotting:
    def line(self ,nr,nd,title,xtitle,ytitle):
        plt.plot(nr,nd)
        plt.xlabel(xtitle)
        plt.ylabel(ytitle)
        # giving a title to my graph
        plt.title(title)
        # function to show the plot
        plt.show()
class Point:
    x=None;	# X-axis coordinates of sensor node
    y=None;	# Y-axis coordinates of sensor node
    def __init__(self,x,y):
        self.y = y
        self.x = x
class Areadiv :
    def div(self,xm,ym,r,midpoint):
        points = []
        ypoints = []
        xpoints = []
        points.append(midpoint)
        i =  1
        while(i*r<(xm-midpoint.x)):
            #print("point",midpoint.y)
            p1 = Point((i*r)+midpoint.x ,midpoint.y)
            xpoints.append(p1)
            points.append(p1)
            i= i+1
        i =  1
        while(((-r)*i)+midpoint.x>0):
            #print("point -----",midpoint.y)
            p2 = Point(((-r)*i)+midpoint.x ,midpoint.y)
            xpoints.append(p2)
            points.append(p2)
            i= i+1    
        i = 1    
        while(i*r<(ym-midpoint.y)):
            p1 = Point(midpoint.x,(i*r) +midpoint.y)
            ypoints.append(p1)
            points.append(p1)
            i= i+1    
        i = 1    
        while(((-r)*i)+midpoint.y>0):
            p2 = Point(midpoint.x,(i*(-r)) +midpoint.y)            
            ypoints.append(p2)
            points.append(p2)
            i= i+1        
        for i  in range(len(xpoints)):
            px = xpoints[i]
            print('px',px.x,px.y)
            for j  in range(len(ypoints)):
                py = ypoints[j]
                p = Point(px.x,py.y)
                points.append(p)
  
        return points        
nodes = []
for i in range(n):
    ry = round(random.random(),2)*ym
    rx = round(random.random(),2)*xm
    dist= math.sqrt((rx-sinkx)**2 + (ry-sinky)**2);
    p= Node(i,rx,ry,Eo,dist)
    nodes.append(p)
    print(nodes[i].x)


plt.plot([sinkx], [sinky], 'r^',label="BS")
for i in range(n):
    plt.plot([nodes[i].x], [nodes[i].y], 'b+',label="N")
    
plt.xlabel('X/m')
plt.ylabel('Y/m') 
midpoint = Point(50,50)
areadiv = Areadiv()
points =areadiv.div(xm,ym,r,midpoint)
pa = points[0]
plt.plot([pa.x], [pa.y],'o',label="N")
for i in range(1,len(points)):
    pa = points[i]
    plt.plot([pa.x], [pa.y],'o',label="N")
plt.show()
#########################################################################################################################################
for i in range(len(nodes)):
    mindch =  math.sqrt((points[0].x-nodes[i].x)**2 + (points[0].y-nodes[i].y)**2)
    index = 0
    for j in range(1,len(points)):
        dch =  math.sqrt((points[j].x-nodes[i].x)**2 + (points[j].y-nodes[i].y)**2)
        if dch <mindch  :
            mindtch = dch 
            index = j
    nodes[i].dch = mindch
    nodes[i].rid = index
########################################################################################################################################
############################################# Set-Up Phase #############################################
nr = []
nd = []
nt = []
ne = []
nch = []
energy=0
groups = Sets()

while operating_nodes>0 :
    for i in range(len(nodes)):
        nodes[i].q = 3/4*nodes[i].e +1/4*1/(nodes[i].dist+nodes[i].dch) 
        nodes[i].chid =-1 
        #print(nodes[i].toString())
        
    indexes =  []    
    for i in range(len(points)):
        minQ = -1
        indexQ = -1
        for j in range(n) :
            if(nodes[j].rid==i and nodes[j].cond ==1 and nodes[j].rwd ==0) : 
                if( minQ == -1) :
                    minQ = nodes[j].q
                    indexQ = j
                else :
                    if minQ<nodes[j].q :
                        minQ = nodes[j].q
                        indexQ = j
        indexes.append([indexQ,i])    
            
    #print(indexes)
    for j in range(len(nodes)) :
        nodes[j].role = 0
        
                
    CHs =[] 
    for i in range(len(indexes)):
        x = indexes[i][0]
        if x != -1:
            nodes[x].role = 1 
            CHs.append(nodes[x])

    for i in range(len(CHs)):
        for j in range(len(nodes)) :                
            if CHs[i].rid == nodes[j].rid :
                dtss =  math.sqrt((CHs[i].x-nodes[j].x)**2 + (CHs[i].y-nodes[j].y)**2)
                nodes[j].dts=dtss
                nodes[j].chid = CHs[i].id    
    
    for j in range(len(nodes)) :  
        nodes[j].rwd= 0 
    for i in range(len(CHs)):
        r = 0
        for j in range(len(nodes)) :  
            if CHs[i].rid == nodes[j].rid  and nodes[j].cond == 1:
                r=r+1
        rd = random.randint(0, r-1)    
        nodes[i].rwd= rd
    """  for j in range(len(nodes)) :
        print(nodes[j].toString())"""
########################################################### ant colony ##################################################################                   
    def bubbleSort(arr) :
        n = len(arr)
        swapped = False
        for  i in range(n-1) :
            for j in range(0,n-i-1) :
                if arr[j].dist < arr[j + 1].dist :
                    swapped = True
                    arr[j],arr[j + 1] = arr[j+1],arr[j]
            if not swapped :
                return 
    bubbleSort(CHs)
    """    for i in range(len(CHs)) :
        print("CHs",CHs[i].dist)"""


    cities = []
    for i in range(len(CHs)):
        city  = City(CHs[i].id,CHs[i].x,CHs[i].y,CHs[i].e,i) 
        cities.append(city)

    city  = City(-1,sinkx,sinky,0,len(cities))
    cities.append(city)
    paths = []
    for kk  in range(len(cities)-1):
        if cities[kk].cType == 0 :
            numberOfCHs = len(CHs)+1
            graph =  [[0 for _ in range(numberOfCHs)] for _ in range(numberOfCHs)]
            for i in range(numberOfCHs):
                for  j in range(numberOfCHs) :
                    x1 = cities[i].x
                    x2 = cities[j].x
                    y1 = cities[i].y
                    y2 = cities[j].y
                    d = round(math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1)), 2)
                    en = cities[i].e 
                    if d<30:
                        graph[i][j]=d+en
                    if(i== numberOfCHs-1) :
                        graph[i][j]=0

            for  i in range(numberOfCHs) :        
                graph[i][kk]=0  
            #print("Graph : "+str(graph))  
            a = AntColonyOptimization(1,1,5,1,20,2,0.5,20,numberOfCHs,graph,kk,numberOfCHs-1)
            a.startAntOptimization()
            best = a.bestTourOrder
            if len(best)== 0 :
                way = [kk, numberOfCHs-1]
                paths.append(way)
            else :
                for kkk  in range(len(best)-1) :
                    cities[best[kkk]].cType = 1 
              
                paths.append(best)
            print("Best Tour Order : "+str(a.bestTourOrder)+ " Best Tour Length : "+ str(a.bestTourLength))   


        
        
    ############################### Energy Dissipation for normal nodes ###############################
    print(paths)
    
    for i in range(len(paths)) :
        st = len(paths[i])
        for j in range(len(paths[i])-1):
            if j+1 == (st-1) :
                #print(CHs[paths[i][j]].id ,"st  -1")
                CHs[paths[i][j]].chid = "ST"
            else :
                CHs[paths[i][j]].chid = CHs[paths[i][j+1]].id
                #print(CHs[paths[i][j]].id ,  CHs[paths[i][j+1]].id)
                
    """for j in range(len(nodes)) :
        print(nodes[j].toString())"""        
    for i in range(n):
        if nodes[i].role == 0 and nodes[i].cond ==1 and len(CHs)>0:
            if nodes[i].e >0 :
                ETx= Eelec*k + Eamp * k * nodes[i].dts**2 
                #print("Etx : ",ETx ," - E : ",nodes[i].dts," = ",nodes[i].e, " ", Eelec ," ",k ," ", Eamp , " ",k , " ",nodes[i].dts, "",2)
                nodes[i].e=nodes[i].e - ETx
                #print("E",nodes[i].e )
                energy=energy+ETx
                if nodes[nodes[i].chid].e>0 and nodes[nodes[i].chid].cond==1 and nodes[nodes[i].chid].role==1 :
                    ERx=(Eelec+EDA)*k
                    energy=energy+ERx
                    nodes[nodes[i].chid].e=nodes[nodes[i].chid].e - ERx
                    if nodes[nodes[i].chid].e<=0 :  #if cluster heads energy depletes with reception
                        nodes[nodes[i].chid].cond=0;
                        #nodes[nodes[i].child].rop=rnd;
                        dead_nodes=dead_nodes +1;
                        operating_nodes= operating_nodes - 1

            if nodes[i].e <=0 :
                dead_nodes=dead_nodes +1;
                operating_nodes= operating_nodes - 1
                nodes[i].cond=0
                nodes[i].chid=-1
                
    for i in range(n):
        if nodes[i].role == 1 and nodes[i].cond ==1 and  nodes[i].chid != "ST":
            if nodes[i].e >0 :
                ETx= (Eelec+EDA)*k + Eamp * k * nodes[i].dist**2;
                nodes[i].e=nodes[i].e - ETx
                energy=energy+ETx
                if nodes[nodes[i].chid].e>0 and nodes[nodes[i].chid].cond==1 and nodes[nodes[i].chid].role==1 :
                    ERx=(Eelec+EDA)*k
                    energy=energy+ERx
                    nodes[nodes[i].chid].e=nodes[nodes[i].chid].e - ERx
                    if nodes[nodes[i].chid].e<=0 :  #if cluster heads energy depletes with reception
                        nodes[nodes[i].chid].cond=0;
                        #nodes[nodes[i].child].rop=rnd;
                        dead_nodes=dead_nodes +1;
                        operating_nodes= operating_nodes - 1
            if nodes[i].e <=0 :
                dead_nodes=dead_nodes +1
                operating_nodes= operating_nodes - 1
                nodes[i].cond=0;
            
        elif nodes[i].role == 1 and nodes[i].cond ==1 and  nodes[i].chid == "ST":
            if nodes[i].e >0 :
                ETx= (Eelec+EDA)*k + Eamp * k * nodes[i].dist**2
                nodes[i].e=nodes[i].e - ETx
                energy=energy+ETx
            if nodes[i].e <=0 :
                dead_nodes=dead_nodes +1
                operating_nodes= operating_nodes - 1
                nodes[i].cond=0;
            

    print() 
    print('#### rnd : ',rnd,'#### transmissions : ' , transmissions ,'#### CHs : ' ,len(CHs) ,'#### dead_nodes : ',dead_nodes," ####") 

    """   for i in range(len(CHs)) : 
        print('CH ID : ',CHs[i].id)"""
    #print('perating_nodes') 
    #print(operating_nodes) 
    print('----------------------------------------------------------------------------------') 
    
    if operating_nodes<n and temp_val==0 :
        temp_val=1
        flag1stdead=rnd
   
    transmissions = transmissions +1
    if(len(CHs)==0): 
        transmissions = transmissions -1
    nr.append([rnd])
    nt.append([transmissions])
    nch.append(len(CHs))
    nd.append([operating_nodes])
    rnd = rnd +1
    if energy>0 :
        ne.append([transmissions,energy])
    if rnd>9000 :
        break
    """for i in range(len(nodes)):
        print(nodes[i].toString())"""
###################################################################################################################
print(paths)
for i in range(len(CHs)):  
    plt.plot([CHs[i].x], [CHs[i].y], 'r^',label="N")
    print(CHs[i].rid) 

plt.plot([sinkx], [sinky], 'r^',label="BS")

for i in range(1,len(points)):
    pa = points[i]
    plt.plot([pa.x], [pa.y],'o',label="N")


for i in range(n):
    plt.plot([nodes[i].x], [nodes[i].y], 'b+',label="N")

for i in range(len(paths)) :
    x = []
    y = []
    for j in range(len(paths[i])) :
        print("id : "+str(cities[paths[i][j]].id) + " x : " +str(cities[paths[i][j]].x)+ " y : " +str(cities[paths[i][j]].y))
        x.append(cities[paths[i][j]].x)
        y.append(cities[paths[i][j]].y)

    plt.plot(x, y)
    # You can specify a rotation for the tick labels in degrees or with keywords.
    plt.xticks(x,  rotation='vertical')
    # Pad margins so that markers don't get clipped by the axes
    plt.margins(0.2)
    # Tweak spacing to prevent clipping of tick-labels
    plt.subplots_adjust(bottom=0.15)

plt.xlabel('X/m')
plt.ylabel('Y/m')
plt.show()

graph = graphPlotting()
graph.line(nr,nd,'Operating Nodes per Round','Rounds','Operational Nodes')
graph.line(nt,nd,'Operating Nodes per Transmissions','Transmissions','Operational Nodes')
sum=0;
for i in range(flag1stdead) :
    sum=ne[i][1] + sum

a = []
b = []
for i in range(flag1stdead) : 
    a.append(ne[i][0])
    b.append(ne[i][1])
    
graph.line(a,b,'Energy consumed per Transmission','Transmission','Energy ( J )')

temp1=sum/flag1stdead
temp2=temp1/n
na= []
for i in range(flag1stdead): 
    na.append(temp2)
graph.line(a,na,'Average Energy consumed by a Node per Transmission','Transmission','Energy ( J )')
graph.line(nt,nch,'CHs per Transmissions','Transmission','CHs')

   