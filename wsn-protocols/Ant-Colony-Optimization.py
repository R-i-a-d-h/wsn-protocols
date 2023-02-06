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
    
        print( self.s)
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

####################################################################################################
#graph=[[0,4,3,1,7],[0,0,6,2,3],[0,0,0,2,1],[0,0,0,0,0],[0,0,0,0,0]]
#graph=[[1,0,1,1],[1,0,1,1],[1,0,1,1],[0,0,0,0]]

graph= [[0.0, 0, 18.11, 13.45, 21.38, 24.35, 21.19, 29.68, 0, 25.55, 29.53, 0, 19.1, 26.83, 25.0, 0, 24.21, 0, 1.0], [15.0, 0, 22.2, 11.66, 0, 0, 29.73, 0, 0, 0, 0, 0, 14.14, 24.19, 15.81, 0, 15.52, 26.48, 70.03], [18.11, 0, 0.0, 28.86, 26.93, 11.18, 0, 19.31, 19.72, 0, 0, 12.04, 0, 0, 18.25, 23.32, 0, 0, 17.12], [13.45, 0, 28.86, 0.0, 0, 0, 18.87, 0, 0, 0, 28.02, 0, 5.66, 14.32, 27.31, 0, 11.18, 19.42, 14.21], [21.38, 0, 26.93, 0, 0.0, 23.02, 27.78, 21.59, 24.74, 17.03, 0, 0, 0, 0, 0, 0, 0, 0, 21.21], [24.35, 0, 11.18, 0, 23.02, 0.0, 0, 8.25, 8.6, 0, 0, 11.4, 0, 0, 29.12, 0, 0, 0, 23.41], [21.19, 0, 0, 18.87, 27.78, 0, 0.0, 0, 0, 16.55, 9.22, 0, 20.88, 19.42, 0, 0, 26.48, 27.66, 22.14], [29.68, 0, 19.31, 0, 21.59, 8.25, 0, 0.0, 3.16, 0, 0, 17.72, 0, 0, 0, 0, 0, 0, 28.84], [0, 0, 19.72, 0, 24.74, 8.6, 0, 3.16, 0.0, 0, 0, 16.12, 0, 0, 0, 0, 0, 0, 0], [25.55, 0, 0, 0, 17.03, 0, 16.55, 0, 0, 0.0, 15.26, 0, 0, 0, 0, 0, 0, 0, 26.08], [29.53, 0, 0, 28.02, 0, 0, 9.22, 0, 0, 15.26, 0.0, 0, 29.55, 26.08, 0, 0, 0, 0, 0], [0, 0, 12.04, 0, 0, 11.4, 0, 17.72, 16.12, 0, 0, 0.0, 0, 0, 24.21, 21.0, 0, 0, 29.02], [19.1, 0, 0, 5.66, 0, 0, 20.88, 0, 0, 0, 29.55, 0, 0.0, 10.05, 29.83, 0, 6.08, 13.89, 19.85], [26.83, 0, 0, 14.32, 0, 0, 19.42, 0, 0, 0, 26.08, 0, 10.05, 0.0, 0, 0, 11.4, 8.25, 27.73], [25.0, 0, 18.25, 27.31, 0, 29.12, 0, 0, 0, 0, 0, 24.21, 29.83, 0, 0.0, 15.13, 0, 0, 24.41], [0, 0, 23.32, 0, 0, 0, 0, 0, 0, 0, 0, 21.0, 0, 0, 15.13, 0.0, 0, 0, 0], [24.21, 0, 0, 11.18, 0, 0, 26.48, 0, 0, 0, 0, 0, 6.08, 11.4, 0, 0, 0.0, 11.05, 24.84], [0, 0, 0, 19.42, 0, 0, 27.66, 0, 0, 0, 0, 0, 13.89, 8.25, 0, 0, 11.05, 0.0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]]

a = AntColonyOptimization(1,1,5,0.5,100,2,0.5,100,5,graph,0,4)
a.startAntOptimization()