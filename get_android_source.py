import os

if __name__=='__main__':
    os.system('dir /s /B *.java > temp')
    source_file = open('temp', 'r')
    output_source = open('android_sourcefiles', 'w')
    for line in source_file:
        if 'pc' in line or 'PC' in line:
            continue
        else:
            output_source.write(line)
    source_file.close()
    os.system('rm temp')
