% Converts the data to csv files. Each component in the third mode is assigned to a separate csv
% file, that is data{k}.csv refers to the matrix: data(:,:,k)
%
% Last modified: 20150825
% Last Edited by: Kohji Muraoka

clear all force; close all force

%% #### START CONFIGURATION #####
fileLoc = 'Fluorescence EEMs'; % should be directly under the current dir
header_size = 3;
%  #### END CONFIGURATION #####

%% file input
addpath(fileLoc);
files = dir(fileLoc);
[m,~] = size(files);

for i=3:m
    filename = files(i,1).name
    IDo{i-2,1} = filename;

    fid=fopen(filename);
    fseek(fid,0,'bof');

    data_toss = importdata(filename);

    try
        EmAx = data_toss.data(1,:);
        for k = 1:125
            ExAx(k) = str2num(data_toss.textdata{header_size+k,1});
        end
        X(i-2,:,:) = data_toss.data(header_size+1:end,:);
    catch
        EmAx = data_toss(1,2:end);
        ExAx = data_toss(2:end,1);
        X(i-2,:,:) = data_toss(2:end,2:end);
    end

    fclose(fid);
end

% Write to csv
for k=1:121
    csvwrite(['data/data' num2str(k) '.csv'], X(:,:,k))
end

