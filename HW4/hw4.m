clear;clc;
%% draw grph
data = csvread('example2.dat');
data = data(:,1:2);
data = unique(data,'rows');
graph0 = zeros(size(data,1),size(data,2));
% remove duplicate edges
for i = 1:size(data,1)
    row = data(i,:);
    new = sort(row);
    graph0(i,:) = new;
end
graph2 = unique(graph0,'rows');
source = graph2(:,1).';
s = num2cell(source);
target = graph2(:,2).';
t = num2cell(target);
G = graph(source,target);
h = plot(G)

%% get eigenvalues
col1 = data(:,1);
col2 = data(:,2);
max_ids = max(max(col1,col2));
As = sparse(col1,col2,1,max_ids,max_ids);
A = full(As);
G = graph(A);
L = laplacian(G);
[v,D] = eigs(L,2,'SA');
% find Fiedler Vector
fv = v(:,2);
p = sort(fv);
figure(2)
h2 = plot(p)

%% define K
prompt = 'What is the approximate value of K';
k = input(prompt);
%% Spectral Clustering
% affinity matrix A = adjacency matrix
adj = zeros(size(A,1),size(A,2));
for i = 1:size(graph2,1)
    pair = graph2(i,:);
    x = pair(1);
    y = pair(2);
    adj(x,y) = 1;
    adj(y,x) = 1;  
end

% define D
D = zeros(size(A,1),size(A,2));
for i = 1:size(A,1)
    ss = sum(adj(i,:));
    D(i,i) = ss^(-1/2);
end

% construct L
L = D*adj*D;

% find k largest eigenvectors of L k=4
[v,d] = eig(L);
X = v(:,size(A,1)-(k-1):size(A,1));

% X = [x1,x2,x3...]
Y = zeros(size(X,1),size(X,2));
for i = 1:size(X,1)
    ss = sumsqr(X(i,:));
    qq = sqrt(ss);
    for j = 1:size(X,2)
        Y(i,j) = X(i,j)/qq;
    end
end
[idx,C] = kmeans(Y,k);

% 
[c, ia, ic] = unique(idx);
hcell = cell(k,1);
%ia = sort(ia);
color = ['g','r','b','c'];
for i = 1:size(ia,1)
    cc = find(idx==i);
    cc = cc';
    hcell{i,1} = cc;
    highlight(h,cc,'NodeColor',color(i),'MarkerSize',3)
end

