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
# Range of transmission
r=40
# Energy Values %%%
#Initial Energy of a Node (in Joules) % 
Eo=2 #units in Joules
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
##################################################################################################################
class Node:
    id=None  # sensor's ID number
    x=None   # X-axis coordinates of sensor node
    y=None   # Y-axis coordinates of sensor node
    e=None   # nodes energy levels (initially set to be equal to "Eo"
    q=None   # Q-value
    rid=None    # the cluster which a node belongs to
    dist = None # nodes distance from the sink
    dch = None  # nodes distance from the cluster head of the cluster in which he belongs
    role = None # node acts as normal if the value is '0', if elected as a cluster head it  gets the value '1' (initially all nodes are normal)
    cond = None # States the current condition of the node. when the node is operational its value is =1 and when dead =0
    chid = None # node ID of the cluster head which the "i" normal node belongs to
    dts = None  # nodes distance from the sink
    rwd = None  # Reward
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
    x=None
    y=None
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
            p1 = Point((i*r)+midpoint.x ,midpoint.y)
            xpoints.append(p1)
            points.append(p1)
            i= i+1
        i =  1
        while(((-r)*i)+midpoint.x>0):
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
##################################################################################################################
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
############################################# Set-Up Phase #############################################
############################################# Set-Up Phase #############################################
nr = []
nd = []
nt = []
ne = []
nch = []
energy=0
while operating_nodes>0 :
    for i in range(len(nodes)):
        nodes[i].q = 3/4*nodes[i].e +1/4*1/(nodes[i].dist+nodes[i].dch)   
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
        if nodes[j].rwd != 0 :
            nodes[j].rwd= nodes[j].rwd-1
                                
    for i in range(len(CHs)):
        r = 0
        for j in range(len(nodes)) :  
            if CHs[i].rid == nodes[j].rid  and nodes[j].cond == 1 and nodes[j].rwd == 0:
                r=r+1
        rd = 0
        if (r-1)!= 0 :
            rd = random.randint(0, r-1)    
        CHs[i].rwd= rd
############################################### Steady-State Phase ########################################################
            
##### Energy Dissipation for normal nodes ####
    for i in range(n):
        if nodes[i].role == 0 and nodes[i].cond ==1 and len(CHs)>0:
            if nodes[i].e >0 :
                ETx= Eelec*k + Eamp * k * nodes[i].dts**2
                nodes[i].e=nodes[i].e - ETx
                energy=energy+ETx
                 # Dissipation for cluster head during reception
                if nodes[nodes[i].chid].e>0 and nodes[nodes[i].chid].cond==1 and nodes[nodes[i].chid].role==1 :
                    ERx=(Eelec+EDA)*k
                    energy=energy+ERx
                    nodes[nodes[i].chid].e=nodes[nodes[i].chid].e - ERx
                    if nodes[nodes[i].chid].e<=0 :  #if cluster heads energy depletes with reception
                        nodes[nodes[i].chid].cond=0
                        dead_nodes=dead_nodes +1;
                        operating_nodes= operating_nodes - 1
            if nodes[i].e <=0 : # if nodes energy depletes with transmission
                dead_nodes=dead_nodes +1
                operating_nodes= operating_nodes - 1
                nodes[i].cond=0
                nodes[i].chid=-1
                
                
#### Energy Dissipation for cluster head nodes ####                
    for i in range(n):
        if nodes[i].role == 1 and nodes[i].cond ==1 :
            if nodes[i].e >0 :
                ETx= (Eelec+EDA)*k + Eamp * k * nodes[i].dist**2
                nodes[i].e=nodes[i].e - ETx
                energy=energy+ETx
            if nodes[i].e <=0 : # if cluster heads energy depletes with transmission
                dead_nodes=dead_nodes +1;
                operating_nodes= operating_nodes - 1
                nodes[i].cond=0
    #Display the information 
    for i in range(len(CHs)) : 
        print('CH ID : ',CHs[i].id)
    print('rnd : ',rnd) 
    print('transmissions : ',transmissions) 
    print('CHs : ', len(CHs)) 
    print('dead_nodes : ' ,dead_nodes) 
    print('perating_nodes : ' ,operating_nodes ) 
    print('----------------------------------------------------------------------------------') 
    if operating_nodes<n and temp_val==0 :
        temp_val=1
        flag1stdead=rnd
    # Next Transmissions
    transmissions = transmissions +1
    if(len(CHs)==0): 
        transmissions = transmissions -1
    nr.append([rnd])
    nt.append([transmissions])
    nch.append(len(CHs))
    nd.append([operating_nodes])
    # Next Round 
    rnd = rnd +1
    if energy>0 :
        ne.append([transmissions,energy])
    if rnd>9999 :
        break
##################################################################################################################
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