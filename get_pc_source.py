import os

if __name__=='__main__':
    os.system('dir /s /B *.java > temp')
    source_file = open('temp', 'r')
    output_source = open('sourcefiles', 'w')
    for line in source_file:
        if 'android' in line or 'MainActivity' in line:
            continue
        else:
            output_source.write(line)
    source_file.close()
    os.system('rm temp')
